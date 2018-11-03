package com.example.mysto.ricknmortybuddykotlin.network.JsonBin

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit


object RetrofitClientInstance {

    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://api.jsonbin.io"

    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}