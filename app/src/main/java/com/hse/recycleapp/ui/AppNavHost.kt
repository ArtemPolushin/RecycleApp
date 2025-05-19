package com.hse.recycleapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hse.recycleapp.ui.auth.LoginScreen
import com.hse.recycleapp.ui.auth.RegisterScreen
import com.hse.recycleapp.ui.map.MapScreen
import com.hse.recycleapp.ui.ml.WasteClassifierScreen
import com.hse.recycleapp.ui.points.EditPointScreen
import com.hse.recycleapp.ui.points.SelectLocationScreen

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
        composable(
            route = "login?redirect={redirect}",
            arguments = listOf(
                navArgument("redirect") {
                    defaultValue = "map"
                }
            )
        ) {
            LoginScreen(navController = navController)
        }


        composable(
            route = "register?redirect={redirect}",
            arguments = listOf(
                navArgument("redirect") {
                    defaultValue = "map"
                }
            )
        ) {
            RegisterScreen(navController = navController)
        }

        composable("map") {
            MapScreen(navController = navController)
        }

        composable("wasteClassifier") {
            WasteClassifierScreen(navController = navController)
        }
        composable("addPoint") {
            EditPointScreen(navController = navController)
        }

        composable("selectLocation") {
            SelectLocationScreen(navController = navController)
        }
    }
}
