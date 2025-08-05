package com.example.workschedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workschedule.Components.*
import java.time.LocalDate
import java.time.LocalDateTime


@Composable
fun Home(navController: NavController) {
    val currentHour = remember { LocalDateTime.now().hour }
    val defaultTime = if (currentHour in 0..11) "Morning" else "Evening"
    var selectedTime by remember { mutableStateOf(defaultTime) }


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
                        TopBar(navController = navController)
                        Spacer(modifier = Modifier.height(20.dp))
                        WeeklyCalendar()
                    }

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
                    Ttems.forEach { task ->
                        TaskItemInteractive(item = task)
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
