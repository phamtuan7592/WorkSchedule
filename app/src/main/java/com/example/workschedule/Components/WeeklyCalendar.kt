package com.example.workschedule.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

@Composable
fun WeeklyCalendar(modifier: Modifier = Modifier) {
    val today = LocalDate.now()
    val startOfWeek = today.with(DayOfWeek.MONDAY)
    val weekDates = (0..6).map { startOfWeek.plusDays(it.toLong()) }

    var selectedDate by remember { mutableStateOf(today) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekDates.forEach { date ->
            val isSelected = date == selectedDate

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .padding(4.dp)
                    .background(
                        color = if (isSelected) Color(0xFF718A73) else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { selectedDate = date },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                        color = if (isSelected) Color.White else Color.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = if (isSelected) Color.White else Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TimeSelector(
    selectedTime: String,
    onTimeSelected: (String) -> Unit
) {
    val options = listOf("Evening", "All Day", "Morning")
    var isAutoSelected by remember { mutableStateOf(true) }


    LaunchedEffect(selectedTime) {
        while (true) {
            val hour = LocalDateTime.now().hour
            val realTime = if (hour in 0..11) "Morning" else "Evening"

            if (selectedTime == "All Day" && isAutoSelected) {
                delay(30_000)
                onTimeSelected(realTime)
                isAutoSelected = true
            }

            delay(60_000)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (option == selectedTime) Color(0xFF4A5A4A) else Color.Transparent
                    )
                    .clickable {
                        onTimeSelected(option)
                        isAutoSelected = false // mark as user override
                    }
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = option,
                    color = Color.White
                )
            }
        }
    }
}
