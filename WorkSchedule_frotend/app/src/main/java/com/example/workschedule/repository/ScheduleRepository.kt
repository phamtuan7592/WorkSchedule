package com.example.workschedule.repository
import com.example.workschedule.model.Schedule
import com.example.workschedule.network.ScheduleApi
import java.io.File


class ScheduleRepository {
    suspend fun addSchedule(schedule: Schedule) = ScheduleApi.postSchedule(schedule)

    suspend fun getSchedules(): List<Schedule> = ScheduleApi.getSchedules()

    suspend fun getScheduleById(id: String): Schedule? = ScheduleApi.getScheduleById(id)

    suspend fun updateSchedule(id: String, schedule: Schedule): Boolean {
        return try {
            val result = ScheduleApi.updateSchedule(id, schedule)
            result != null
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    suspend fun deleteSchedule(id: String): Boolean {
        return try {
            val success = ScheduleApi.deleteSchedule(id)
            success
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}