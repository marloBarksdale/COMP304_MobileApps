package com.example.lyndenflood_comp304lab3_ex1.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: TipsApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.api-ninjas.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TipsApiService::class.java)
    }
}
