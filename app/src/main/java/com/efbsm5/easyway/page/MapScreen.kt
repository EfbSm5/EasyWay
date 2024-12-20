package com.efbsm5.easyway.page

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun EasyWay() {
    val navControl = rememberNavController()
    NavHost(navController = navControl, startDestination = "MapPage") {
        composable("MapPage") {
            MapPage { navControl.navigate("SocietyPage") }
        }
        composable("SocietyPage") {
            PersonalPage()
        }
    }
}


