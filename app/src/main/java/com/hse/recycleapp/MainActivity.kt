package com.hse.recycleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.hse.recycleapp.ui.AppNavHost
import com.hse.recycleapp.ui.theme.RecycleAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecycleAppTheme {
                val navController = rememberNavController()

                AppNavHost(navController = navController)
            }
        }
    }
}