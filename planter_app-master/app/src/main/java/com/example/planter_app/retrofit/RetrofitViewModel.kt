package com.example.planter_app.retrofit

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planter_app.BuildConfig
import com.example.planter_app.retrofit.ml_model_api.ClassificationRequestItem
import com.example.planter_app.retrofit.ml_model_api.MLModelRetrofitInstance
import com.example.planter_app.retrofit.ml_model_api.MLModelRetrofitInstance.mlModelApiService
import com.example.planter_app.retrofit.plant_net_api.PlantNetRetrofitInstance
import com.example.planter_app.retrofit.plant_net_api.Root
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File

class RetrofitViewModel() : ViewModel() {
    private val apiKey = BuildConfig.plantNetApikey

    private val _apiResponseLoading = MutableStateFlow(false)
    val apiResponseLoading: StateFlow<Boolean> = _apiResponseLoading.asStateFlow()

    fun setApiResponseLoading(boolean: Boolean){
        _apiResponseLoading.value = boolean
    }

    fun plantNetUploadImage(filePath: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(filePath)
            try {
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val image = MultipartBody.Part.createFormData("images", file.name, requestFile)

                val response: Response<Root> = PlantNetRetrofitInstance.create(apiKey).uploadImage(
                    apiKey = apiKey,
                    images = listOf(image),
                    organs = listOf()
                )

                if (response.isSuccessful) {
                    val root = response.body()
                    if (root != null) {
                        val result = root.results.getOrNull(0) // 0th index has the highest accuracy/score
                        val commonNamesList = result?.species?.commonNames ?: emptyList()
                        val scientificNameWithoutAuthor = result?.species?.family?.scientificNameWithoutAuthor?: ""

                        val returnResponse = if (commonNamesList.isNotEmpty()) {
                            commonNamesList[0]
                        } else if (scientificNameWithoutAuthor.isNotBlank()) {
                            scientificNameWithoutAuthor
                        } else {
                            "No Results found"
                        }
                        onSuccess(returnResponse)

                    } else {
                        onError("Empty response body")
                    }
                } else {
                    onError(response.code().toString())
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }


    fun mLModelUploadImage(imagePath: String, onSuccessResult: (String) -> Unit,onSuccessAdvice: (String) -> Unit, onError: (String) -> Unit){
        encodeImageFileToBase64(imagePath) { base64Image ->
            val request = ClassificationRequestItem(base64Image)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = mlModelApiService.classifyImage(request)
                    if (response.isSuccessful) {
                        val result = response.body()?.result
                        val advice = response.body()?.advice

                        if (result != null && advice!=null) {
                            onSuccessResult(result)
                            onSuccessAdvice(advice)
                        } else {
                            onError("Unknown error: Response body is null")
                        }
                    } else {
                        onError("Error: ${response.code()} - ${response.message()}")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown error")
                }

            }
        }
    }

    private fun encodeImageFileToBase64(imagePath: String, onEncoded: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            val file = File(imagePath)
            val bytes = file.readBytes()
            val encodedString = Base64.encodeToString(bytes, Base64.DEFAULT)
            onEncoded(encodedString) // Call the callback with the encoded string
        }
    }

}

