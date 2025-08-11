package com.example.workschedule.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.workschedule.model.Schedule
import com.example.workschedule.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.time.LocalTime

class ScheduleViewModel(
    private val repository: ScheduleRepository = ScheduleRepository()
) : ViewModel() {

    private  val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedule: StateFlow<List<Schedule>> = _schedules

    fun getSchedules(){
        viewModelScope.launch {
            _schedules.value = repository.getSchedules()
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

            val request = Schedule(
                title = title,
                description = description,
                selectedDays = selectedDays,
                endDate = if (endDateEnabled) selectedDate.toString() else null,
                time = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
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

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
