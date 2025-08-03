package com.example.workschedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.workschedule.Components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var soundEnabled by remember { mutableStateOf(true) }
    var repeatEnabled by remember { mutableStateOf(true) }
    var endDateEnabled by remember { mutableStateOf(false) }
    var goalEnabled by remember { mutableStateOf(false) }
    var selectedFrequency by remember { mutableStateOf("Everyday") }
    var selectedUnit by remember { mutableStateOf("Time") }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFDFF3EA), shape = RoundedCornerShape(50.dp))
                        .clickable {
                            // TODO: xử lý khi bấm nút
                        }
                        .padding( vertical = 20.dp),
                         contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Save",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B6B)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF4B4B4B))
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSection(navController = navController)
            Spacer(modifier = Modifier.height(24.dp))

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

            SectionCard {
                SoundSection(soundEnabled = soundEnabled)
                Spacer(modifier = Modifier.height(20.dp))
                ToggleRow("Repeat", repeatEnabled) { repeatEnabled = it }
                if (repeatEnabled) {
                    Spacer(modifier = Modifier.height(16.dp))
                    DropdownSection(
                        selectedValue = selectedFrequency,
                        onValueChange = { selectedFrequency = it },
                        options = listOf("Everyday", "Weekly", "Monthly", "Custom")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            SectionCard {
                ToggleRow("End date", endDateEnabled) { endDateEnabled = it }
            }

            Spacer(modifier = Modifier.height(12.dp))

//            SectionCard {
//                ToggleRow("Goal", goalEnabled) { goalEnabled = it }
//                Spacer(modifier = Modifier.height(16.dp))
//                Text("Units", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
//                Spacer(modifier = Modifier.height(8.dp))
//                UnitsSection(
//                    selectedUnit = selectedUnit,
//                    onUnitChange = { selectedUnit = it }
//                )
//            }

            SectionCard {
                DropdownSection(
                    selectedValue = "Time of the day",
                    onValueChange = { },
                    options = listOf("Time of the day", "Morning", "Afternoon", "Evening")
                )
            }

            Spacer(modifier = Modifier.height(80.dp)) // Để không che bởi nút
        }
    }
}

@Composable
fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF5A665F), shape = RoundedCornerShape(20.dp))
            .padding(14.dp),
        content = content
    )
}
