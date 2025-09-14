package com.example.workschedule.Components

import com.example.workschedule.model.Schedule
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale

    fun isValidByEndDate(schedule: Schedule, selectedDate: LocalDate): Boolean {
        return schedule.endDate?.let { endDateStr ->
            try {
                val endDate = LocalDate.parse(endDateStr)
                selectedDate.isBefore(endDate) || selectedDate.isEqual(endDate)
            } catch (e: Exception) {
                true
            }
        } ?: true
    }

    fun matchesSelectedDay(schedule: Schedule, selectedDate: LocalDate): Boolean {
        return schedule.selectedDays.isEmpty() ||
                schedule.selectedDays.contains("Everyday") ||
                schedule.selectedDays.contains(
                    selectedDate.dayOfWeek.getDisplayName(
                        TextStyle.FULL,
                        Locale.ENGLISH
                    )
                )
    }

fun matchesSelectedTime(schedule: Schedule, selectedTime: String): Boolean {
    val taskTime = LocalTime.parse(schedule.time ?: "00:00")
    return when (selectedTime) {
        "All Day" -> true
        "Morning" -> taskTime.isBefore(LocalTime.NOON) // 00:00–11:59
        "Noon" -> taskTime >= LocalTime.NOON && taskTime.isBefore(LocalTime.of(17, 0)) // 12:00–16:59
        "Evening" -> taskTime >= LocalTime.of(17, 0) // 17:00+
        else -> true
    }
}


fun filterSchedules(searchText: String, schedules: List<Schedule>): List<Schedule> {
    return if (searchText.isBlank()) {
        emptyList()
    } else {
        schedules.filter { schedule ->
            schedule.title.contains(searchText, ignoreCase = true)
        }
    }
}

    fun filterAndSortTasks(
        schedules: List<Schedule>,
        selectedDate: LocalDate,
        selectedTime: String
    ): List<Schedule> {
        return schedules
            .filter { schedule ->
                isValidByEndDate(schedule, selectedDate) &&
                        matchesSelectedDay(schedule, selectedDate) &&
                        matchesSelectedTime(schedule, selectedTime)
            }
            .sortedBy { schedule ->
                LocalTime.parse(schedule.time ?: "00:00")
            }
    }



