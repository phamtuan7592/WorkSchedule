package com.example.viewmodel

import com.example.model.Schedule
import com.example.repository.ScheduleRepository
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScheduleViewModel(private val repository: ScheduleRepository) {

    suspend fun addSchedule(schedule: Schedule): Schedule {
        return repository.addSchedule(schedule)
    }

    suspend fun getSchedules(): List<Schedule> {
        return repository.getSchedules()
    }

    suspend fun getScheduleById(id: String): Schedule? {
        return repository.getScheduleById(id)
    }

//    suspend fun addSchedule(schedule: Schedule): Schedule {
//        val finalEndDate = schedule.endDate ?: getNextEndDate(schedule.selectedDays.toList())
//        val result = schedule.copy(endDate = finalEndDate)
//        return repository.addSchedule(result)
//    }
//
//    suspend fun getSchedules(): List<Schedule> {
//        return repository.getSchedules()
//    }
//
//    suspend fun getScheduleById(id: String): Schedule? {
//        return repository.getScheduleById(id)
//    }
//
//    private fun getNextEndDate(selectedDays: List<String>): String {
//        val daysOfWeek = selectedDays.mapNotNull {
//            try {
//                DayOfWeek.valueOf(it.uppercase())
//            } catch (e: Exception) {
//                null
//            }
//        }
//
//        if (daysOfWeek.isEmpty()) {
//            throw IllegalArgumentException("selectedDays không hợp lệ")
//        }
//
//        val lastDay = daysOfWeek.last()
//        val now = LocalDateTime.now()
//
//        var resultDate = now.toLocalDate()
//        while (resultDate.dayOfWeek != lastDay) {
//            resultDate = resultDate.plusDays(1)
//        }
//
//        resultDate = resultDate.plusDays(1)
//
//        val fullDateTime = LocalDateTime.of(resultDate, now.toLocalTime())
//        return fullDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
//    }
}
