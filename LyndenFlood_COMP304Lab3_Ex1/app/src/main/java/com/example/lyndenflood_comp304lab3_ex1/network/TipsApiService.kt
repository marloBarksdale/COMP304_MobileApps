package com.example.lyndenflood_comp304lab3_ex1.network

import com.example.lyndenflood_comp304lab3_ex1.model.HealthTip
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface TipsApiService {
    @GET("exercises")
    suspend fun getHealthTips(
        @Header("X-Api-Key") apiKey: String,
       @Query("type") type: String = "cardio"
    ): List<HealthTip>
}
