package com.example.workschedule.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workschedule.model.Schedule
import com.example.workschedule.repository.ScheduleRepository
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class ScheduleViewModel(private val repository: ScheduleRepository = ScheduleRepository()) : ViewModel() {

    fun saveSchedule(
        context: Context,
        title: String,
        description: String,
        selectedDays: Set<String>,
        endDateEnabled: Boolean,
        selectedDate: Any?,
        selectedTime: java.time.LocalTime?,
        repeatEnabled: Boolean
    ) {
        viewModelScope.launch {
            if (title.isBlank()) {
                Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
                return@launch
            }
            if (description.isBlank()) {
                Toast.makeText(context, "Please enter a description", Toast.LENGTH_SHORT).show()
                return@launch
            }
            if (repeatEnabled && selectedDays.isEmpty()) {
                Toast.makeText(context, "Please select repeat days", Toast.LENGTH_SHORT).show()
                return@launch
            }
            if (selectedTime == null) {
                Toast.makeText(context, "Please select a time", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val request = Schedule(
                title = title,
                description = description,
                selectedDays = selectedDays,
                endDate = if (endDateEnabled) selectedDate.toString() else null,
                time = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            )

            val success = repository.addSchedule(request)
            Toast.makeText(context, if (success) "Saved successfully" else "Failed to save", Toast.LENGTH_SHORT).show()
        }
    }
}
