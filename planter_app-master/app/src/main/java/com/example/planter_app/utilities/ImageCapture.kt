package com.example.planter_app.utilities

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import cafe.adriel.voyager.navigator.Navigator
import com.example.planter_app.ui.screens.my_plants.plant_details.PlantDetails
import java.io.File
import java.io.FileOutputStream
import java.util.Objects

@Composable
fun captureImageFromCamera(navigator: Navigator): Triple<Uri, ManagedActivityResultLauncher<String, Boolean>, ManagedActivityResultLauncher<Uri, Boolean>> {
    val context = LocalContext.current
    val file = File.createTempFile(
        "imageFileName",
        ".jpg",
        context.externalCacheDir

    )
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture())
        { isTaken ->
            if (isTaken) {
                // Save Uri to the gallery and get the file path
                val photoFilePathUri = saveUriToGallery(uri, context)

                photoFilePathUri?.let {
                    navigator.push(PlantDetails( photoFilePathUri))
                }
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { permissionGranted->
            if (permissionGranted) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    return Triple(uri, permissionLauncher, cameraLauncher)
}

private fun saveUriToGallery(uri: Uri, context: Context): String? {
    val inputStream = context.contentResolver.openInputStream(uri)
    val picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val myDir = File(picturesDirectory, "Plant Health Advisor")
    if (!myDir.exists()) {
        myDir.mkdirs()
    }
    val fileName = "IMG_${System.currentTimeMillis()}.jpg"
    val file = File(myDir, fileName)

    return try {
        FileOutputStream(file).use { out ->
            inputStream?.copyTo(out)
        }
        // Notify the media scanner about the new file so that it is immediately available to the user.
        MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), null, null)
        file.absolutePath
    } catch (e: Exception) {
        Log.e("SaveUri", "Error saving uri to gallery", e)
        null
    } finally {
        inputStream?.close()
    }
}