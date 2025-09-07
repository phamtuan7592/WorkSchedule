package com.example.workschedule.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import com.example.workschedule.R

@Composable
fun HeaderSection(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        //.height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Content",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.back1),
                contentDescription = "back",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { navController.popBackStack() },
                contentScale = ContentScale.FillBounds
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    minHeight: Dp = 56.dp
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = minHeight)
            .background(
                color = Color(0xFF414C46),
                shape = RoundedCornerShape(20.dp)
            ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            cursorColor = Color.White,
            focusedContainerColor = Color(0xFF414C46),
            unfocusedContainerColor = Color(0xFF414C46)
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun SoundSection(
    onRecordClick: () -> Unit,
    onPlayClick: () -> Unit,
    isRecording: Boolean,
    isPlaying: Boolean,
    recordingTime: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.VolumeUp,
            contentDescription = "Sound",
            tint = Color(0xFFFF5252),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Sound",
            color = Color.White,
            fontSize = 16.sp
        )

        if (isRecording) {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = recordingTime,
                color = Color.Red,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(if (isRecording) Color.Red else Color(0xFFFF5252))
                .clickable { onRecordClick() },
            contentAlignment = Alignment.Center
        ) {
            if (isRecording) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isPlaying) Color.Blue else Color(0xFF4CAF50))
                .clickable { onPlayClick() },
            contentAlignment = Alignment.Center
        ) {
            if (isPlaying) {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = "Pause",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun ToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFFFF5252),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFF5A5A5A)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSection(
    selectedValue: String,
    onValueChange: (String) -> Unit,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = { },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.White
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF3A4A3D),
                unfocusedBorderColor = Color(0xFF3A4A3D),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF4A5A4A))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(text = option, color = Color.White)
                    },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun UnitsSection(
    selectedUnit: String,
    onUnitChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Units",
            color = Color.White,
            fontSize = 16.sp
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF2A3D2D))
                .padding(2.dp)
        ) {
            UnitButton(
                text = "Units",
                isSelected = selectedUnit == "Units",
                onClick = { onUnitChange("Units") }
            )
            UnitButton(
                text = "Time",
                isSelected = selectedUnit == "Time",
                onClick = { onUnitChange("Time") }
            )
        }
    }
}

@Composable
fun UnitButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (isSelected) Color(0xFFFF5252) else Color.Transparent
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
fun MultiDayPicker(
    selectedDays: Set<String>,
    onSelectionChange: (Set<String>) -> Unit
) {
    val allDays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val dayAbbreviations = mapOf(
        "Monday" to "Mon",
        "Tuesday" to "Tue",
        "Wednesday" to "Wed",
        "Thursday" to "Thu",
        "Friday" to "Fri",
        "Saturday" to "Sat",
        "Sunday" to "Sun"
    )

    val displayText = when {
        selectedDays.contains("Everyday") || selectedDays == allDays.toSet() -> "Everyday"
        selectedDays.size == 1 -> selectedDays.first()
        selectedDays.size >= 2 -> {
            allDays.filter { selectedDays.contains(it) }
                .joinToString(", ") { dayAbbreviations[it] ?: it }
        }
        else -> "Select days"
    }

    var expanded by remember { mutableStateOf(false) }

    Column {
        // Display selected days
        OutlinedTextField(
            value = displayText,
            onValueChange = { },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF3A4A3D),
                unfocusedBorderColor = Color(0xFF3A4A3D),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Dropdown menu
        if (expanded) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4A5A4A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    // Everyday option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (selectedDays.contains("Everyday")) {
                                    onSelectionChange(emptySet())
                                } else {
                                    onSelectionChange(setOf("Everyday"))
                                }
                                expanded = false
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedDays.contains("Everyday") || selectedDays == allDays.toSet(),
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFFFF5252),
                                uncheckedColor = Color.Gray,
                                checkmarkColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Everyday",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    HorizontalDivider(
                        color = Color(0xFF5A5A5A),
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    // Individual days in correct order
                    allDays.forEach { day ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val newSelection = if (selectedDays.contains("Everyday")) {
                                        setOf(day)
                                    } else {
                                        val updated = if (selectedDays.contains(day)) {
                                            selectedDays - day
                                        } else {
                                            selectedDays + day
                                        }

                                        if (updated == allDays.toSet()) {
                                            setOf("Everyday")
                                        } else {
                                            updated
                                        }
                                    }
                                    onSelectionChange(newSelection)
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = !selectedDays.contains("Everyday") && selectedDays.contains(day),
                                onCheckedChange = null,
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFFFF5252),
                                    uncheckedColor = Color.Gray,
                                    checkmarkColor = Color.White,
                                    disabledCheckedColor = Color.Gray,
                                    disabledUncheckedColor = Color.Gray.copy(alpha = 0.3f)
                                ),
                                enabled = !selectedDays.contains("Everyday")
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = day,
                                color = if (selectedDays.contains("Everyday")) Color.Gray else Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


// Cập nhật Preview để test MultiDayPicker
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun PreviewAllComponents() {
    val context = LocalContext.current
    val navController = remember { TestNavHostController(context) }
    var selectedDays by remember { mutableStateOf(setOf<String>("Everyday")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        HeaderSection(navController = navController)
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField("Example", {}, "Enter text")
        Spacer(modifier = Modifier.height(16.dp))
        SoundSection(
            onRecordClick = {},
            onPlayClick = {},
            isRecording = false,
            isPlaying = false,
            recordingTime = "00:00"
        )
        Spacer(modifier = Modifier.height(16.dp))
        ToggleRow("Enable feature", true, {})
        Spacer(modifier = Modifier.height(16.dp))
        DropdownSection("Option A", {}, listOf("Option A", "Option B"))
        Spacer(modifier = Modifier.height(16.dp))
        MultiDayPicker(
            selectedDays = selectedDays,
            onSelectionChange = { selectedDays = it }
        )
    }
}