package com.example.workschedule.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

enum class ScreenTheme {
    Default, // Dành cho WorkSchedule
    Content  // Dành cho ContentApp
}

// ---------- WorkSchedule ColorScheme ----------
private val WorkDarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val WorkLightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

// ---------- ContentApp ColorScheme ----------
private val ContentDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF6B6B),
    secondary = Color(0xFF4CAF50),
    tertiary = Color(0xFF2C3E2D),
    background = Color(0xFF2C3E2D),
    surface = Color(0xFF2C3E2D),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val ContentLightColorScheme = lightColorScheme(
    primary = Color(0xFFFF6B6B),
    secondary = Color(0xFF4CAF50),
    tertiary = Color(0xFF2C3E2D),
    background = Color(0xFF2C3E2D),
    surface = Color(0xFF2C3E2D),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

// ---------- Typography mặc định (bạn có thể tuỳ chỉnh lại) ----------
private val AppTypography = Typography()

// ---------- Theme chung ----------
@Composable
fun AppTheme(
    screenTheme: ScreenTheme = ScreenTheme.Default,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when (screenTheme) {
        ScreenTheme.Default -> {
            if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            } else {
                if (darkTheme) WorkDarkColorScheme else WorkLightColorScheme
            }
        }

        ScreenTheme.Content -> {
            if (darkTheme) ContentDarkColorScheme else ContentLightColorScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
