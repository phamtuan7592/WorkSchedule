package com.example.workschedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.workschedule.Components.TaskItemInteractive
import com.example.workschedule.model.Schedule
import com.example.workschedule.viewmodel.ScheduleViewModel
import com.example.workschedule.Components.filterAndSortTasks
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
) {
    val viewModel: ScheduleViewModel = viewModel()

    // Load dữ liệu từ DB
    LaunchedEffect(Unit) {
        viewModel.getSchedules()
    }

    val schedules by viewModel.schedule.collectAsState()

    var startDate by remember { mutableStateOf((selectedDate ?: LocalDate.now()).with(DayOfWeek.MONDAY)) }
    val today = LocalDate.now()

    val formattedDate = today.format(
        DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale("vi"))
    )

    val timeSlots = listOf("Morning", "Noon", "Evening") // đồng bộ với Home

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2C3333))
            .padding(16.dp)
    ) {
        // ===== Header =====
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        navController.popBackStack("home", inclusive = false)
                    }
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Calendar",
                    color = Color.White,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(start = 12.dp),
                )
            }

            IconButton(
                onClick = { navController.navigate("month_calendar") }
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Open Month Calendar",
                    tint = Color.White
                )
            }
        }

        Text(
            text = formattedDate,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ===== Điều hướng tuần =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { startDate = startDate.minusWeeks(1) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008B8B)),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous Week", tint = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = { startDate = LocalDate.now().with(DayOfWeek.MONDAY) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008B8B)),
                modifier = Modifier.height(48.dp)
            ) {
                Icon(Icons.Default.DateRange, contentDescription = "Today", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("HIỆN TẠI", color = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = { startDate = startDate.plusWeeks(1) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008B8B)),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next Week", tint = Color.White)
            }
        }

        // ===== Lịch tuần =====
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Cột thời gian
            Column {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(70.dp)
                        .background(Color(0xFF55605A)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Hoạt động", color = Color.White, textAlign = TextAlign.Center)
                }

                Spacer(modifier = Modifier.height(8.dp))

                timeSlots.forEach { time ->
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(180.dp)
                            .background(Color(0xFF55605A))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(time, color = Color.White, textAlign = TextAlign.Center)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Cột ngày trong tuần
            val weekDates = (0..6).map { startDate.plusDays(it.toLong()) }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(weekDates) { date ->
                    Column {
                        // Header ngày
                        Box(
                            modifier = Modifier
                                .width(150.dp)
                                .height(70.dp)
                                .background(
                                    if (date == today) Color(0xFF444444) else Color(0xFF7B2D2D)
                                )
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("vi")),
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = if (date == today) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    text = "${date.dayOfMonth}/${date.monthValue}",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Các ô thời gian
                        timeSlots.forEach { slot ->
                            val slotTasks = filterAndSortTasks(schedules, date, slot)

                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(180.dp) // khung cố định
                                    .background(Color(0xFF91A19A), shape = RoundedCornerShape(8.dp))
                                    .padding(4.dp),
                            ) {
                                if (slotTasks.isEmpty()) {
                                    Text("—", color = Color.White, textAlign = TextAlign.Center)
                                } else {
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        items(slotTasks) { task ->
                                            ActivityItemCompact(
                                                item = task,
                                                onClick = {
                                                    navController.navigate("detail/${task.id}")
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityItemCompact(
    item: Schedule,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2D2D2D), RoundedCornerShape(6.dp))
            .padding(6.dp)
            .heightIn(min = 40.dp, max = 60.dp), // giới hạn chiều cao
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = item.title,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
        item.time?.let {
            Text(
                text = it,
                color = Color(0xFFB0B0B0),
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}

// Hàm phân loại buổi
fun matchTimeSlot(taskTime: String, slot: String): Boolean {
    return when (slot) {
        "Morning" -> taskTime.contains("AM", ignoreCase = true) || taskTime.contains("Sáng")
        "Noon" -> taskTime.contains("Trưa")
        "Evening" -> taskTime.contains("PM", ignoreCase = true) || taskTime.contains("Tối")
        else -> false
    }
}
