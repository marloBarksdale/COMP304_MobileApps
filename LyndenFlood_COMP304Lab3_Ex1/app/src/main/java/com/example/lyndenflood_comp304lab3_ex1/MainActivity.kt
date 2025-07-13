package com.example.lyndenflood_comp304lab3_ex1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.lyndenflood_comp304lab3_ex1.navigation.AppNavGraph
import com.example.lyndenflood_comp304lab3_ex1.ui.theme.LyndenFlood_COMP304Lab3_Ex1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LyndenFlood_COMP304Lab3_Ex1Theme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}