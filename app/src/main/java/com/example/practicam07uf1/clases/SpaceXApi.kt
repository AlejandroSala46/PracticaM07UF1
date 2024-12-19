package com.example.practicam07uf1.clases

import retrofit2.Call
import retrofit2.http.GET

//Interfaz para acceder a la API
interface SpaceXApi {
    @GET("v4/rockets")
    fun getRockets(): Call<List<RocketEntity>>
}
