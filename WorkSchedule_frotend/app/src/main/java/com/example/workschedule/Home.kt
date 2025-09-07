package com.example.workschedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.workschedule.Components.*
import com.example.workschedule.model.Schedule
import com.example.workschedule.viewmodel.ScheduleViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*

@Composable
fun Home(navController: NavController) {
    val viewModel: ScheduleViewModel = viewModel()

    val context = LocalContext.current


    // Load schedules
    LaunchedEffect(Unit) {
        viewModel.getSchedules()
    }

    val schedules by viewModel.schedule.collectAsState()
    val currentHour = remember { LocalDateTime.now().hour }
    val defaultTime = if (currentHour in 0..11) "Morning" else "Evening"
    var selectedTime by remember { mutableStateOf(defaultTime) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val filteredTasks = remember(selectedDate, selectedTime, schedules) {
        filterAndSortTasks(schedules, selectedDate, selectedTime)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4B4B4B))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFB8EBC0))
                        .padding(bottom = 16.dp)
                ) {
                    Column {
                        TopBar()
                        Spacer(modifier = Modifier.height(20.dp))
                        WeeklyCalendar(
                            selectedDate = selectedDate,
                            onDateSelected = { selectedDate = it }
                        )
                    }

                    // Giữ nguyên các decorative elements
                    Column {
                        Image(
                            painter = painterResource(R.drawable.rectangle11),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            contentScale = ContentScale.FillBounds
                        )

                        Image(
                            painter = painterResource(R.drawable.vector),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillBounds
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.chim),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .offset(x = 60.dp, y = 2.dp),
                                contentScale = ContentScale.Fit
                            )
                            Image(
                                painter = painterResource(R.drawable.chim),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .offset(x = 110.dp, y = 10.dp),
                                contentScale = ContentScale.Fit
                            )
                            Image(
                                painter = painterResource(R.drawable.chim),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .offset(x = 60.dp, y = 40.dp),
                                contentScale = ContentScale.Fit
                            )

                            when (selectedTime) {
                                "Morning" -> {
                                    Image(
                                        painter = painterResource(R.drawable.sun),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .offset(x = 200.dp, y = (-10).dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                                "All Day" -> {
                                    Image(
                                        painter = painterResource(R.drawable.sun1),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .offset(x = 270.dp, y = (-40).dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                                "Evening" -> {
                                    Image(
                                        painter = painterResource(R.drawable.moon),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .align(Alignment.Center)
                                            .offset(y = 20.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF4B4B4B))
                ) {
                    Image(
                        painter = painterResource(R.drawable.vector1),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        contentScale = ContentScale.FillBounds
                    )
                    Image(
                        painter = painterResource(R.drawable.tree),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .offset(x = 300.dp, y = 8.dp),
                        contentScale = ContentScale.Fit
                    )
                    Image(
                        painter = painterResource(R.drawable.tree),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .offset(x = 350.dp, y = 13.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TimeSelector(selectedTime = selectedTime, onTimeSelected = { selectedTime = it })

                    // Hiển thị các task đã lọc
                    filteredTasks.forEach { task ->
                        TaskItemInteractive(
                            item = task,
                            navController = navController,
                            onDeleteClick = { scheduleToDelete ->
                                viewModel.deleteSchedule(scheduleToDelete, context)
                            }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            ButtonBar(navController = navController)
        }
    }
}