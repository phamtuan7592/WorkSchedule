package com.example.workschedule

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.workschedule.Components.MonthCalendarScreen
import com.example.workschedule.ui.theme.WorkScheduleTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Đã cấp quyền ghi âm", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Từ chối quyền ghi âm", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    fun requestAudioPermission() {
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkScheduleTheme {
                AppNavigation(this)
            }
        }
    }
}


@Composable
fun AppNavigation(mainActivity: MainActivity) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home"){
        composable("home") { Home(navController) }
        composable("search") { Search(navController)}
        composable("content") { ContentScreen(navController, mainActivity) }
        composable("habits") { Habits(navController) }
        composable(
            route = "edit/{scheduleId}",
            arguments = listOf(navArgument("scheduleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val scheduleId = backStackEntry.arguments?.getString("scheduleId") ?: ""
            EditScheduleScreen(navController = navController, editId = scheduleId, mainActivity = mainActivity)
        }
        composable("calendar/{date}") { backStackEntry ->
            val dateArg = backStackEntry.arguments?.getString("date")
            val selectedDate = dateArg?.let { LocalDate.parse(it) }
            CalendarScreen(navController, selectedDate = selectedDate)
        }
        composable("calendar") { CalendarScreen(navController) }
        composable("month_calendar") { MonthCalendarScreen(navController) }

    }
}