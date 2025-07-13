package com.example.lyndenflood_comp304lab3_ex1.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lyndenflood_comp304lab3_ex1.ui.screens.AddActivityScreen
import com.example.lyndenflood_comp304lab3_ex1.ui.screens.EditActivityScreen
import com.example.lyndenflood_comp304lab3_ex1.ui.screens.StatsScreen
import com.example.lyndenflood_comp304lab3_ex1.ui.screens.TipsScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavRoutes.Home.route) {
        composable(NavRoutes.Home.route) {
            HomeScreen(navController)
        }
        composable(NavRoutes.AddActivity.route) {
            AddActivityScreen(navController)
        }
        composable(NavRoutes.Stats.route) {
            StatsScreen(navController)
        }
        composable(NavRoutes.Tips.route) {
            TipsScreen(navController)
        }
        composable("edit_activity/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (id != null) {
                EditActivityScreen(navController, id)
            }
        }

    }
}
