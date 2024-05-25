package com.example.planter_app.retrofit.plant_net_api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface PlantNetApiService {
    @Multipart
    @POST("/v2/identify/{project}")
    suspend fun uploadImage(
        @Path("project") project: String = "all",
        @Query("include-related-images") includeRelatedImages: Boolean = false,
        @Query("no-reject") noReject: Boolean = false,
        @Query("lang") lang: String = "en",
        @Query("type") type: String = "kt",
        @Query("api-key") apiKey: String,
        @Part images: List<MultipartBody.Part>,
        @Part organs: List<MultipartBody.Part>
    ): Response<Root>
}
