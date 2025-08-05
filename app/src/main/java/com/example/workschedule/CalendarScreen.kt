package com.example.workschedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavHostController
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
    var startDate by remember { mutableStateOf((selectedDate ?: LocalDate.now()).with(DayOfWeek.MONDAY)) }
    val today = LocalDate.now()

    val formattedDate = today.format(
        DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale("vi"))
    )

    val timeSlots = listOf("BUỔI SÁNG", "BUỔI TRƯA", "BUỔI TỐI")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2C3333))
            .padding(16.dp)
    ) {
        // header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { navController.popBackStack(
                        route = "home",
                        inclusive = false
                    ) }
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
        // hien tai
        Text(
            text = formattedDate,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        //dieu huong tuan
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //tuan truoc
            Button(
                onClick = { startDate = startDate.minusWeeks(1) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008B8B)),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous Week", tint = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            //hom nay
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

            //tuan sau
            Button(
                onClick = { startDate = startDate.plusWeeks(1) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008B8B)),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next Week", tint = Color.White)
            }
        }

        //lich su viec
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            //thoi gian trong ngay
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

            //ngay trong tyan
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
                                    text = "${date.dayOfMonth}/${date.monthValue}/${date.year}",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        //o thoi gian
                        timeSlots.forEach { _ ->
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(180.dp)
                                    .background(
                                        Color(0xFF91A19A),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("", color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}
