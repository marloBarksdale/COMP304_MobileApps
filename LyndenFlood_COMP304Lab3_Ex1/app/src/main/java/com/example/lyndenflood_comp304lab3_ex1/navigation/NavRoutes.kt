package com.example.lyndenflood_comp304lab3_ex1.navigation

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object AddActivity : NavRoutes("add_activity")
    object Stats : NavRoutes("stats")
    object Tips : NavRoutes("tips")
}
