package com.example.lyndenflood_comp304lab3_ex1.repository

import com.example.lyndenflood_comp304lab3_ex1.BuildConfig
import com.example.lyndenflood_comp304lab3_ex1.model.HealthTip
import com.example.lyndenflood_comp304lab3_ex1.network.RetrofitInstance


class TipsRepository {
    suspend fun fetchTips(): List<HealthTip> {

//      val hardcodedKey = "lhE6cAO5ZqD7meu3f0w3Jw==IZR8dVBFYmFk5wz1"
//        return RetrofitInstance.api.getHealthTips(hardcodedKey, "cardio")

      return RetrofitInstance.api.getHealthTips(
           apiKey =  BuildConfig.API_NINJAS_KEY,
           type = "cardio",

       )
    }
}
