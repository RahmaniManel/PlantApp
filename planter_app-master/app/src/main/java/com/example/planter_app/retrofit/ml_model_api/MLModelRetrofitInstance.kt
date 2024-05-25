package com.example.planter_app.retrofit.ml_model_api

import com.example.planter_app.retrofit.plant_net_api.PlantNetApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MLModelRetrofitInstance {
    private const val BASE_URL = "https://plant-health-advisor.onrender.com"

    private val httpClient by lazy {
        OkHttpClient.Builder().apply {

            addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
        }.build()
    }

    // Create the Retrofit instance using lazy initialization
    val mlModelApiService: MLModelApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(MLModelApiService::class.java)
    }
}
