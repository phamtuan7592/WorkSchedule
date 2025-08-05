package com.example.workschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.workschedule.Components.MonthCalendarScreen
import com.example.workschedule.ui.theme.WorkScheduleTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkScheduleTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home"){
        composable("home") { Home(navController) }
        composable("search") { Search(navController)}
        composable("content") { ContentScreen(navController) }
        composable("habits") { Habits(navController) }
        composable("calendar/{date}") { backStackEntry ->
            val dateArg = backStackEntry.arguments?.getString("date")
            val selectedDate = dateArg?.let { LocalDate.parse(it) }
            CalendarScreen(navController, selectedDate = selectedDate)
        }
        composable("calendar") { CalendarScreen(navController) }
        composable("month_calendar") { MonthCalendarScreen(navController) }
    }
}
