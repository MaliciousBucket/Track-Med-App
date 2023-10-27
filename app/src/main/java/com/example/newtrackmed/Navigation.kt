package com.example.newtrackmed

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newtrackmed.ui.feature.home.HomeScreen

@Composable
fun TrackMedNavController() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                // onMyMedicationsClick = { navController.navigate("my_medications_route") },
                // onReportsClick = {}
            )
        }
    }
}