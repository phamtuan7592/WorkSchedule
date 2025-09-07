package com.example.viewmodel

import com.example.model.Schedule
import com.example.repository.ScheduleRepository
import org.slf4j.LoggerFactory

class ScheduleViewModel(private val repository: ScheduleRepository) {
    private val logger = LoggerFactory.getLogger(ScheduleViewModel::class.java)

    suspend fun addSchedule(schedule: Schedule): Schedule {
        return repository.addSchedule(schedule)
    }

    suspend fun getSchedules(): List<Schedule> {
        return repository.getSchedules()
    }

    suspend fun getScheduleById(id: String): Schedule? {
        require(id.isNotBlank()) { "ID cannot be empty" }
        return repository.getScheduleById(id)
    }

    suspend fun updateSchedule(id: String, newSchedule: Schedule): Schedule? {
        require(id.isNotBlank()) { "ID cannot be empty" }
        require(newSchedule.title.isNotBlank()) { "Title cannot be empty" }
        require(newSchedule.selectedDays.isNotEmpty()) { "At least one day must be selected" }

        return repository.updateSchedule(id, newSchedule)
    }

    suspend fun deleteSchedule(id: String): Boolean {
        require(id.isNotBlank()) { "ID cannot be empty" }
        return repository.deleteSchedule(id)
    }

    suspend fun scheduleExists(id: String): Boolean {
        return repository.scheduleExists(id)
    }
}

