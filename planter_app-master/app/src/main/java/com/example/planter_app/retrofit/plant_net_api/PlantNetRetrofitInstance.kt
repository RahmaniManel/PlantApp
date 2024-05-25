package com.example.planter_app.retrofit.plant_net_api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//https://my.plantnet.org/doc/openapi

object PlantNetRetrofitInstance {
    private const val BASE_URL = "https://my-api.plantnet.org/"

    fun create(apiKey: String): PlantNetApiService {
        val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("X-Api-Key", apiKey)
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(PlantNetApiService::class.java)
    }

}