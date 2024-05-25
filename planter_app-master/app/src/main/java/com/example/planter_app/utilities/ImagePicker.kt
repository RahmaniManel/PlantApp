package com.example.planter_app.utilities

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import cafe.adriel.voyager.navigator.Navigator
import com.example.planter_app.MyApplication
import com.example.planter_app.ui.screens.my_plants.plant_details.PlantDetails

@Composable
fun singlePhotoPickerFromGallery(
    navigator: Navigator
): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri!=null) {
                val filePathUri = getRealPathFromURI(uri)

                filePathUri?.let {
                    navigator.push(PlantDetails(filePathUri))
                }
            }
        }
    )
    return singlePhotoPicker
}

fun getRealPathFromURI(uri: Uri): String? {
    val context = MyApplication.instance!!.applicationContext
    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
    return cursor?.use {
        it.moveToFirst()
        val columnIndex: Int = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val filePath: String = it.getString(columnIndex)
        it.close()
        filePath
    }
}
