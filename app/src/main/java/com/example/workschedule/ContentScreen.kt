package com.example.workschedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.workschedule.Components.CustomTextField
import com.example.workschedule.Components.DropdownSection
import com.example.workschedule.Components.HeaderSection
import com.example.workschedule.Components.SoundSection
import com.example.workschedule.Components.ToggleRow


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
                    options = listOf("Everyday", "Weekly", "Monthly", "Custom")
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // End date toggle
        SectionCard {
            ToggleRow("End date", endDateEnabled) { endDateEnabled = it }
        }

//        Spacer(modifier = Modifier.height(12.dp))

        // Goal + Units
//        SectionCard {
//            ToggleRow("Goal", goalEnabled) { goalEnabled = it }
//            Spacer(modifier = Modifier.height(16.dp))
//            Text("Units", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
//            Spacer(modifier = Modifier.height(8.dp))
//            com.example.workschedule.Components.UnitsSection(
//                selectedUnit = selectedUnit,
//                onUnitChange = { selectedUnit = it }
//            )
//        }

        Spacer(modifier = Modifier.height(12.dp))

        // Time of the day
        SectionCard {
            DropdownSection(
                selectedValue = "Time of the day",
                onValueChange = { },
                options = listOf("Time of the day", "Morning", "Afternoon", "Evening")
            )
        }
    }
}
@Composable
fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4A5A4A ), shape = RoundedCornerShape(20.dp))
            .padding(14.dp),
        content = content
    )
}

