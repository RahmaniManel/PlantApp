package com.example.planter_app.retrofit.ml_model_api

import com.example.planter_app.retrofit.plant_net_api.Root
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST

interface MLModelApiService {
    @POST("/classify")
    suspend fun classifyImage(
        @Body request: ClassificationRequestItem
    ): Response<ClassificationResponse>
}

data class ClassificationRequestItem(
    val imageInput: String
)

data class ClassificationResponse(
    val result: String,
    val advice: String
)