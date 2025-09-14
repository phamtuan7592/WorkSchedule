package com.example.workschedule.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.workschedule.Cloudinary.Cloudinary
import com.example.workschedule.model.Schedule
import com.example.workschedule.network.ScheduleApi
import com.example.workschedule.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.LocalTime

class ScheduleViewModel(
    private val repository: ScheduleRepository = ScheduleRepository()
) : ViewModel() {

    private  val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedule: StateFlow<List<Schedule>> = _schedules

    private val _filteredSchedules = MutableStateFlow<List<Schedule>>(emptyList())
    val filteredSchedules: StateFlow<List<Schedule>> = _filteredSchedules

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    private val _uploadProgress = MutableStateFlow(0f)
    val uploadProgress: StateFlow<Float> = _uploadProgress.asStateFlow()

    private val _uploadError = MutableStateFlow<String?>(null)
    val uploadError: StateFlow<String?> = _uploadError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedSchedule = MutableStateFlow<Schedule?>(null)
    val selectedSchedule: StateFlow<Schedule?> = _selectedSchedule

    fun getSchedules(){
        viewModelScope.launch {
            _schedules.value = repository.getSchedules()
        }
    }

    fun getScheduleById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                println("ViewModel: Getting schedule with ID: $id")
                val schedule = repository.getScheduleById(id)
                println("ViewModel: Received schedule: $schedule")
                _selectedSchedule.value = schedule

                if (schedule == null) {
                    println("ViewModel: Schedule is null!")
                } else {
                    println("ViewModel: Schedule details - Title: ${schedule.title}, Description: ${schedule.description}")
                }
            } catch (e: Exception) {
                println("ViewModel error: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun saveSchedule(
        context: Context,
        title: String,
        description: String,
        selectedDays: Set<String>,
        endDateEnabled: Boolean,
        selectedDate: Any?,
        selectedTime: LocalTime?,
        repeatEnabled: Boolean,
        audioUri: Uri?, // Đổi từ audioFilePath sang audioUri
        navController: NavController
    ) {
        viewModelScope.launch {
            // Validate input
            when {
                title.isBlank() -> {
                    showToast(context, "Please enter a title")
                    return@launch
                }
                description.isBlank() -> {
                    showToast(context, "Please enter a description")
                    return@launch
                }
                repeatEnabled && selectedDays.isEmpty() -> {
                    showToast(context, "Please select repeat days")
                    return@launch
                }
                selectedTime == null -> {
                    showToast(context, "Please select a time")
                    return@launch
                }
            }

            if (audioUri != null) {
                uploadAudioToCloudinary(context, audioUri, title, description, selectedDays,
                    endDateEnabled, selectedDate, selectedTime, repeatEnabled, navController)
            } else {
                saveScheduleToRepository(
                    context = context,
                    title = title,
                    description = description,
                    selectedDays = selectedDays,
                    endDateEnabled = endDateEnabled,
                    selectedDate = selectedDate,
                    selectedTime = selectedTime,
                    audioUrl = null,
                    navController = navController
                )
            }
        }
    }

    private fun uploadAudioToCloudinary(
        context: Context,
        audioUri: Uri,
        title: String,
        description: String,
        selectedDays: Set<String>,
        endDateEnabled: Boolean,
        selectedDate: Any?,
        selectedTime: LocalTime?,
        repeatEnabled: Boolean,
        navController: NavController
    ) {
        _isUploading.value = true
        _uploadError.value = null

        val filePath = audioUri.path ?: run {
            _isUploading.value = false
            _uploadError.value = "Invalid audio file path"
            return
        }

        Cloudinary.uploadAudio(
            filePath = filePath,
            onSuccess = { cloudinaryUrl ->
                // Upload thành công, lưu schedule với URL từ Cloudinary
                saveScheduleToRepository(
                    context = context,
                    title = title,
                    description = description,
                    selectedDays = selectedDays,
                    endDateEnabled = endDateEnabled,
                    selectedDate = selectedDate,
                    selectedTime = selectedTime,
                    audioUrl = cloudinaryUrl,
                    navController = navController
                )
                _isUploading.value = false
            },
            onError = { error ->
                // Upload thất bại, vẫn lưu schedule nhưng không có audio URL
                _uploadError.value = "Audio upload failed: $error"
                showToast(context, "Audio upload failed, saving without audio")

                saveScheduleToRepository(
                    context = context,
                    title = title,
                    description = description,
                    selectedDays = selectedDays,
                    endDateEnabled = endDateEnabled,
                    selectedDate = selectedDate,
                    selectedTime = selectedTime,
                    audioUrl = null,
                    navController = navController
                )
                _isUploading.value = false
            },
            onProgress = { bytes, totalBytes ->
                // Cập nhật tiến trình upload
                _uploadProgress.value = if (totalBytes > 0) {
                    bytes.toFloat() / totalBytes.toFloat()
                } else {
                    0f
                }
            }
        )
    }

    private fun saveScheduleToRepository(
        context: Context,
        title: String,
        description: String,
        selectedDays: Set<String>,
        endDateEnabled: Boolean,
        selectedDate: Any?,
        selectedTime: LocalTime?,
        audioUrl: String?,
        navController: NavController
    ) {
        viewModelScope.launch {
            val request = Schedule(
                title = title,
                description = description,
                selectedDays = selectedDays,
                endDate = if (endDateEnabled) selectedDate.toString() else null,
                time = selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm")),
                audioUrl = audioUrl // Sử dụng URL từ Cloudinary thay vì local path
            )

            val success = repository.addSchedule(request)

            if (success) {
                showToast(context, "Saved successfully")
                navController.navigate("habits")
            } else {
                showToast(context, "Failed to save")
            }
        }
    }

    fun clearUploadState() {
        _uploadProgress.value = 0f
        _isUploading.value = false
        _uploadError.value = null
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun updateSchedule(
        context: Context,
        id: String,
        title: String,
        description: String,
        selectedDays: Set<String>,
        endDateEnabled: Boolean,
        selectedDate: LocalDate?,
        selectedTime: LocalTime?,
        repeatEnabled: Boolean,
        audioUri: Uri?, // file âm thanh mới
        onSuccess: () -> Unit
    ) {
        if (!validateInput(context, title, description, selectedDays, selectedTime, repeatEnabled)) return

        viewModelScope.launch {
            _isLoading.value = true
            _isUploading.value = audioUri != null

            if (audioUri != null) {
                // Upload audio trước khi update
                val filePath = audioUri.path ?: run {
                    _isUploading.value = false
                    _isLoading.value = false
                    showToast(context, "Invalid audio file path")
                    return@launch
                }

                Cloudinary.uploadAudio(
                    filePath,
                    onSuccess = { cloudinaryUrl ->
                        updateScheduleToRepository(
                            context, id, title, description, selectedDays,
                            endDateEnabled, selectedDate, selectedTime,
                            repeatEnabled, cloudinaryUrl, onSuccess
                        )
                        _isUploading.value = false
                    },
                    onError = { error ->
                        showToast(context, "Audio upload failed, updating without audio")
                        updateScheduleToRepository(
                            context, id, title, description, selectedDays,
                            endDateEnabled, selectedDate, selectedTime,
                            repeatEnabled, audioUrl = null, onSuccess
                        )
                        _isUploading.value = false
                    },
                    onProgress = { bytes, totalBytes ->
                        _uploadProgress.value = if (totalBytes > 0) bytes.toFloat() / totalBytes.toFloat() else 0f
                    }
                )
            } else {
                // Không có audio -> update trực tiếp
                updateScheduleToRepository(
                    context, id, title, description, selectedDays,
                    endDateEnabled, selectedDate, selectedTime,
                    repeatEnabled, audioUrl = null, onSuccess
                )
            }

            _isLoading.value = false
        }
    }

    // Hàm update lên repository
    private fun updateScheduleToRepository(
        context: Context,
        id: String,
        title: String,
        description: String,
        selectedDays: Set<String>,
        endDateEnabled: Boolean,
        selectedDate: LocalDate?,
        selectedTime: LocalTime?,
        repeatEnabled: Boolean,
        audioUrl: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val updatedSchedule = Schedule(
                id = id,
                title = title,
                description = description,
                selectedDays = if (repeatEnabled) selectedDays else emptySet(),
                endDate = if (endDateEnabled) selectedDate?.toString() else null,
                time = selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm")),
                audioUrl = audioUrl
            )

            val success = repository.updateSchedule(id, updatedSchedule)

            if (success) {
                showToast(context, "Schedule updated successfully")
                getSchedules()
                onSuccess()
            } else {
                showToast(context, "Failed to update schedule")
            }
        }
    }



    // Hàm tách riêng để thực hiện update schedule với audioUrl đã upload (hoặc null)
    private suspend fun performUpdateSchedule(
        context: Context,
        id: String,
        title: String,
        description: String,
        selectedDays: Set<String>,
        endDateEnabled: Boolean,
        selectedDate: LocalDate?,
        selectedTime: LocalTime?,
        repeatEnabled: Boolean,
        audioUrl: String?,
        onSuccess: () -> Unit
    ) {
        val updatedSchedule = Schedule(
            id = id,
            title = title,
            description = description,
            selectedDays = if (repeatEnabled) selectedDays else emptySet(),
            endDate = if (endDateEnabled) selectedDate?.toString() else null,
            time = selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm")),
            audioUrl = audioUrl // Cập nhật audioUrl mới
        )

        val success = repository.updateSchedule(id, updatedSchedule)

        if (success) {
            showToast(context, "Schedule updated successfully")
            getSchedules() // refresh danh sách
            onSuccess()
        } else {
            showToast(context, "Failed to update schedule")
        }
    }


    private fun validateInput(
        context: Context,
        title: String,
        description: String,
        selectedDays: Set<String>,
        selectedTime: LocalTime?,
        repeatEnabled: Boolean
    ): Boolean {
        return when {
            title.isBlank() -> {
                showToast(context, "Please enter a title"); false
            }
            description.isBlank() -> {
                showToast(context, "Please enter a description"); false
            }
            repeatEnabled && selectedDays.isEmpty() -> {
                showToast(context, "Please select repeat days"); false
            }
            selectedTime == null -> {
                showToast(context, "Please select a time"); false
            }
            else -> true
        }
    }

    fun clearSelectedSchedule() {
        _selectedSchedule.value = null
    }

    fun deleteSchedule(schedule: Schedule, context: Context) {
        viewModelScope.launch {
            try {
                val success = ScheduleApi.deleteSchedule(schedule.id)
                if (success) {
                    Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
                    getSchedules() // refresh danh sách
                } else {
                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

}