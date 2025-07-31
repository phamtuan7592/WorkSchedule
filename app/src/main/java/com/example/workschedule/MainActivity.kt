package com.example.workschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.workschedule.ui.theme.AppTheme
import com.example.workschedule.ui.theme.ScreenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "content") {
        composable("home") {
            AppTheme(screenTheme = ScreenTheme.Default) {
                Home(navController)
            }
        }

        composable("content") {
            AppTheme(screenTheme = ScreenTheme.Content) {
                ContentScreen(navController)
            }
        }
    }
}
