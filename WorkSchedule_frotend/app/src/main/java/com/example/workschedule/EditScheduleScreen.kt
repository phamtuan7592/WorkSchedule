package com.example.workschedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.workschedule.Components.CustomTextField
import com.example.workschedule.Components.HeaderSection
import com.example.workschedule.Components.MultiDayPicker
import com.example.workschedule.Components.SoundSection
import com.example.workschedule.Components.ToggleRow
import com.example.workschedule.play.AudioPlayer
import com.example.workschedule.record.AudioRecorder
import com.example.workschedule.viewmodel.ScheduleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.let
import kotlin.text.isNotEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScheduleScreen(
    navController: NavController,
    editId: String,
    viewModel: ScheduleViewModel = viewModel(),
    mainActivity: MainActivity
) {
    val context = LocalContext.current

    // Collect states
    val selectedSchedule by viewModel.selectedSchedule.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Form states
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var repeatEnabled by remember { mutableStateOf(false) }
    var endDateEnabled by remember { mutableStateOf(false) }
    var selectedDays by remember { mutableStateOf(setOf<String>()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var dataLoaded by remember { mutableStateOf(false) }

    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var recordingTime by remember { mutableStateOf(0L) }
    var audioUri by remember { mutableStateOf<Uri?>(null) }


    val uploadError by viewModel.uploadError.collectAsState()

    val audioRecorder = remember { AudioRecorder(context) }
    val audioPlayer = remember { AudioPlayer() }

    // Recording timer
    LaunchedEffect(isRecording) {
        if (isRecording) {
            while (isRecording) {
                delay(1000L)
                recordingTime++
            }
        } else {
            recordingTime = 0L
        }
    }

    val formattedTime = remember(recordingTime) {
        val minutes = recordingTime / 60
        val seconds = recordingTime % 60
        String.format("%02d:%02d", minutes, seconds)
    }

    // Xử lý lỗi upload
    LaunchedEffect(uploadError) {
        uploadError?.let { error ->
            Toast.makeText(context, "Upload error: $error", Toast.LENGTH_LONG).show()
            viewModel.clearUploadState()
        }
    }

    val onRecordClick = onRecordClick@{
        if (!mainActivity.checkAudioPermission()) {
            mainActivity.requestAudioPermission()
            return@onRecordClick
        }

        coroutineScope.launch {
            if (isRecording) {
                audioRecorder.stopRecording()
                isRecording = false
                audioUri = audioRecorder.getRecordedUri()
                Toast.makeText(context, "Đã dừng ghi âm", Toast.LENGTH_SHORT).show()
            } else {
                // Xóa file cũ nếu có trước khi ghi mới
                audioRecorder.deleteRecordedFile()

                val newUri = audioRecorder.startRecording()
                if (newUri != null) {
                    isRecording = true
                    audioUri = newUri
                    Toast.makeText(context, "Bắt đầu ghi âm...", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Lỗi khi bắt đầu ghi âm", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val onPlayClick = {
        if (isPlaying) {
            audioPlayer.stopAudio()
            isPlaying = false
        } else if (audioUri != null) {
            audioPlayer.playAudio(audioUri!!) {
                isPlaying = false
            }
            isPlaying = true
        } else {
            Toast.makeText(context, "Không có bản ghi âm", Toast.LENGTH_SHORT).show()
        }
    }


    // Load schedule when screen opens
    LaunchedEffect(editId) {
        if (editId.isNotEmpty()) {
            println("Loading schedule with ID: $editId")
            viewModel.getScheduleById(editId)
        } else {
            println("Error: Empty schedule ID!")
        }
    }

    // Update form when schedule is loaded
    LaunchedEffect(selectedSchedule) {
        selectedSchedule?.let { schedule ->
            if (!dataLoaded) {
                println("Filling form with data: ${schedule.title}")

                title = schedule.title
                description = schedule.description
                selectedDays = schedule.selectedDays
                repeatEnabled = schedule.selectedDays.isNotEmpty()
                endDateEnabled = !schedule.endDate.isNullOrEmpty()

                // Parse date safely
                selectedDate = try {
                    if (!schedule.endDate.isNullOrEmpty()) {
                        LocalDate.parse(schedule.endDate)
                    } else null
                } catch (e: Exception) {
                    println("Error parsing date: ${e.message}")
                    null
                }

                // Parse time safely
                selectedTime = try {
                    if (!schedule.time.isNullOrEmpty()) {
                        LocalTime.parse(schedule.time)
                    } else null
                } catch (e: Exception) {
                    println("Error parsing time: ${e.message}")
                    null
                }

                dataLoaded = true
                println("Form filled - Title: $title, Description: $description, Time: $selectedTime")
            }
        }
    }

    // Clear selected schedule when leaving screen
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedSchedule()
        }
    }

    Scaffold(
        containerColor = Color(0xFF4B4B4B),
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
                        .background(
                            if (isLoading) Color(0xFFCCCCCC) else Color(0xFFDFF3EA),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable(enabled = !isLoading && dataLoaded) {
                            viewModel.updateSchedule(
                                context = context,
                                id = editId,
                                title = title,
                                description = description,
                                selectedDays = selectedDays,
                                endDateEnabled = endDateEnabled,
                                selectedDate = selectedDate,
                                selectedTime = selectedTime,
                                repeatEnabled = repeatEnabled,
                                onSuccess = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color(0xFFFF6B6B),
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Update Schedule",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B6B),
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSection(navController = navController)
            Spacer(modifier = Modifier.height(12.dp))

            // Title & Description Section
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

            // Repeat Section
            SectionCard {
                SoundSection(
                    onRecordClick = onRecordClick,
                    onPlayClick = onPlayClick,
                    isRecording = isRecording,
                    isPlaying = isPlaying,
                    recordingTime = formattedTime
                )
                Spacer(modifier = Modifier.height(20.dp))
                ToggleRow("Repeat", repeatEnabled) {
                    repeatEnabled = it
                    if (!it) {
                        selectedDays = emptySet()
                    }
                }
                if (repeatEnabled) {
                    Spacer(modifier = Modifier.height(16.dp))
                    MultiDayPicker(
                        selectedDays = selectedDays,
                        onSelectionChange = { selectedDays = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // End Date Section
            SectionCard {
                ToggleRow("End date", endDateEnabled) {
                    endDateEnabled = it
                    if (!it) {
                        selectedDate = null
                    }
                }

                if (endDateEnabled) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF414C46), shape = RoundedCornerShape(12.dp))
                            .clickable { showDatePicker = true }
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Calendar Icon",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = selectedDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                    ?: "Select end date",
                                color = if (selectedDate != null) Color.White else Color.White.copy(alpha = 0.6f),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Time Section
            SectionCard {
                Text(
                    text = "Time of the day",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF414C46), shape = RoundedCornerShape(12.dp))
                        .clickable { showTimePicker = true }
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Time Icon",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm"))
                                ?: "Select time",
                            color = if (selectedTime != null) Color.White else Color.White.copy(alpha = 0.6f),
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Debug info (remove in production)
            if (selectedSchedule != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Debug: Loaded ${selectedSchedule?.title} - ${selectedSchedule?.description}",
                    color = Color.Yellow,
                    fontSize = 12.sp
                )
            }
        }

        // Date Picker Dialog
        if (showDatePicker) {
            val today = LocalDate.now()
            val dialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    showDatePicker = false
                },
                selectedDate?.year ?: today.year,
                (selectedDate?.monthValue ?: today.monthValue) - 1,
                selectedDate?.dayOfMonth ?: today.dayOfMonth
            )
            dialog.datePicker.minDate = System.currentTimeMillis()
            dialog.setOnDismissListener { showDatePicker = false }
            dialog.show()
        }

        // Time Picker Dialog
        if (showTimePicker) {
            val currentTime = selectedTime ?: LocalTime.now()
            val dialog = TimePickerDialog(
                context,
                { _, hour, minute ->
                    selectedTime = LocalTime.of(hour, minute)
                    showTimePicker = false
                },
                currentTime.hour,
                currentTime.minute,
                true
            )
            dialog.setOnDismissListener { showTimePicker = false }
            dialog.show()
        }
    }
}