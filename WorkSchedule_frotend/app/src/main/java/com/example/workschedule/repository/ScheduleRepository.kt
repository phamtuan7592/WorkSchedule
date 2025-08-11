package com.example.workschedule.repository

import com.example.workschedule.model.Schedule
import com.example.workschedule.network.ScheduleApi

class ScheduleRepository {
    suspend fun addSchedule(schedule: Schedule) = ScheduleApi.postSchedule(schedule)

    suspend fun getSchedules(): List<Schedule> = ScheduleApi.getSchedules()
}