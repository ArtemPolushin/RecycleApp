package com.hse.recycleapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hse.recycleapp.ui.auth.LoginScreen
import com.hse.recycleapp.ui.auth.RegisterScreen
import com.hse.recycleapp.ui.map.MapScreen
import com.hse.recycleapp.ui.ml.WasteClassifierScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = "map"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }

        composable("register") {
            RegisterScreen(navController = navController)
        }

        composable("map") {
            MapScreen(navController = navController)
        }

        composable("wasteClassifier") {
            WasteClassifierScreen(navController = navController)
        }
    }
}
