package com.example.workschedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.workschedule.Components.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(navController: NavController) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var soundEnabled by remember { mutableStateOf(true) }
    var repeatEnabled by remember { mutableStateOf(true) }
    var endDateEnabled by remember { mutableStateOf(false) }
    var goalEnabled by remember { mutableStateOf(false) }

    var selectedFrequency by remember { mutableStateOf("Everyday") }
    var selectedUnit by remember { mutableStateOf("Time") }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedValue by remember { mutableStateOf("Time of the day") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4B4B4B))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        HeaderSection(navController = navController)

        Spacer(modifier = Modifier.height(12.dp))

        // Title + Description
        SectionCard {
            CustomTextField(
                value = title,
                onValueChange = { title = it },
                label = "Title"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                value = description,
                onValueChange = { description = it },
                label = "Description",
                minHeight = 100.dp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sound + Repeat
        SectionCard {
            SoundSection(soundEnabled = soundEnabled)
            Spacer(modifier = Modifier.height(20.dp))
            ToggleRow("Repeat", repeatEnabled) { repeatEnabled = it }
            if (repeatEnabled) {
                Spacer(modifier = Modifier.height(16.dp))
                DropdownSection(
                    selectedValue = selectedFrequency,
                    onValueChange = { selectedFrequency = it },
                    options = listOf(
                        "Everyday", "Monday", "Tuesday", "Wednesday",
                        "Thursday", "Friday", "Saturday", "Sunday"
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // End Date Picker
        SectionCard {
            ToggleRow("End date", endDateEnabled) { endDateEnabled = it }

            if (endDateEnabled) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { showDatePicker = true }
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Calendar Icon",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = selectedDate?.toString() ?: "Select a date",
                        color = Color.White
                    )
                }

                if (showDatePicker) {
                    val today = LocalDate.now()
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                            showDatePicker = false
                        },
                        today.year,
                        today.monthValue - 1,
                        today.dayOfMonth
                    ).show()
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Time of the day
        SectionCard {
           Text(
               text = "Time of the day",
               color = Color.White
           )

            if (showTimePicker) {
                val now = LocalTime.now()
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val time = LocalTime.of(hour, minute)
                        selectedTime = time
                        selectedValue = time.format(DateTimeFormatter.ofPattern("HH:mm"))
                        showTimePicker = false
                    },
                    now.hour,
                    now.minute,
                    true
                ).show()
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { showTimePicker = true }
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Time Icon",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Select a time",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF414C46), shape = RoundedCornerShape(20.dp))
            .padding(14.dp),
        content = content
    )
}
