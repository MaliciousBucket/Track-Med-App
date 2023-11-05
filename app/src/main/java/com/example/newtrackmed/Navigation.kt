package com.example.newtrackmed

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newtrackmed.ui.feature.addmedication.AddMedicationScreen
import com.example.newtrackmed.ui.feature.home.HomeScreen
import com.example.newtrackmed.ui.feature.mymedications.MyMedicationsScreen
import com.example.newtrackmed.ui.feature.report.NewReportsScreen


sealed class Screens(val route: String) {
    object Home: Screens("home_screen")
    object MyMedications : Screens ("my_medications_screen")
    object Reports: Screens ("reports_screen")

    object AddMedication: Screens("add_medication_screen")
}
@Composable
fun TrackMedNavController() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ){
        composable(route = Screens.Home.route){
            HomeScreen(
                onNavToMyMeds = { navController.navigate(Screens.MyMedications.route) },
                onNavigateToReports = {navController.navigate(Screens.Reports.route)},
                onNavToAddMedication = {navController.navigate(Screens.AddMedication.route)}
            )
        }
        composable(route = Screens.MyMedications.route){
            MyMedicationsScreen(
                onNavigateToHomeScreen = {navController.navigate(Screens.Home.route)},
                onNavigateToReportsScreen = {navController.navigate(Screens.Reports.route)},
                onNavigateToAddMedication = {navController.navigate(Screens.AddMedication.route)}
            )

        }

        composable(route = Screens.Reports.route){
            NewReportsScreen(
                onNavToHome = {navController.navigate(Screens.Home.route)},
                onNaveToMyMedications = { navController.navigate(Screens.MyMedications.route) }
            )
        }

        composable(route = Screens.AddMedication.route){
            AddMedicationScreen(
                onNavBackPressed = { /*TODO*/ },
                onMedicationSaved = { navController.navigate(Screens.MyMedications.route) }
            )
        }
    }
}
