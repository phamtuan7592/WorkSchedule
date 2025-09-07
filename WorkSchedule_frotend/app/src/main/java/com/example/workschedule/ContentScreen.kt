package com.example.workschedule

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.workschedule.Components.*
import com.example.workschedule.play.AudioPlayer
import com.example.workschedule.record.AudioRecorder
import com.example.workschedule.viewmodel.ScheduleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(navController: NavController, mainActivity: MainActivity) {
    val context = LocalContext.current
    val viewModel: ScheduleViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var repeatEnabled by remember { mutableStateOf(true) }
    var endDateEnabled by remember { mutableStateOf(false) }

    var selectedDays by remember { mutableStateOf(setOf<String>()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Audio states
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
                        .background(Color(0xFFDFF3EA), shape = RoundedCornerShape(50.dp))
                        .clickable {
                            // Gọi saveSchedule với audioUri
                            viewModel.saveSchedule(
                                context = context,
                                title = title,
                                description = description,
                                selectedDays = selectedDays,
                                endDateEnabled = endDateEnabled,
                                selectedDate = selectedDate,
                                selectedTime = selectedTime,
                                repeatEnabled = repeatEnabled,
                                audioUri = audioUri,
                                navController = navController
                            )
                        }
                        .padding(vertical = 20.dp),
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
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                HeaderSection(navController = navController)
                Spacer(modifier = Modifier.height(12.dp))

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
                    SoundSection(
                        onRecordClick = onRecordClick,
                        onPlayClick = onPlayClick,
                        isRecording = isRecording,
                        isPlaying = isPlaying,
                        recordingTime = formattedTime
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    ToggleRow("Repeat", repeatEnabled) { repeatEnabled = it }
                    if (repeatEnabled) {
                        Spacer(modifier = Modifier.height(16.dp))
                        MultiDayPicker(
                            selectedDays = selectedDays,
                            onSelectionChange = { selectedDays = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                SectionCard {
                    ToggleRow("End date", endDateEnabled) { endDateEnabled = it }

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

                        if (showDatePicker) {
                            val today = LocalDate.now()
                            val dialog = android.app.DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                                    showDatePicker = false
                                },
                                today.year,
                                today.monthValue - 1,
                                today.dayOfMonth
                            )
                            dialog.datePicker.minDate = System.currentTimeMillis()
                            dialog.setOnDismissListener { showDatePicker = false }
                            dialog.show()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

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

                    if (showTimePicker) {
                        val now = LocalTime.now()
                        val dialog = android.app.TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                selectedTime = LocalTime.of(hour, minute)
                                showTimePicker = false
                            },
                            now.hour,
                            now.minute,
                            true
                        )
                        dialog.setOnDismissListener { showTimePicker = false }
                        dialog.show()
                    }
                }
            }

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