package com.example.practicam07uf1.clases

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Creamos la instancia de retrofit
object RetrofitClient {
    private const val BASE_URL = "https://api.spacexdata.com/"

    val instance: SpaceXApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(SpaceXApi::class.java)
    }
}
