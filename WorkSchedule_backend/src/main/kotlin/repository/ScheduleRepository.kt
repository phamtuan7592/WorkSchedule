package com.example.repository

import com.example.data.MongoDbClient
import com.example.model.Schedule
import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.litote.kmongo.eq

class ScheduleRepository {
    private val collection = MongoDbClient.database.getCollection<Schedule>("schedules")

    suspend fun addSchedule(schedule: Schedule): Schedule {
        collection.insertOne(schedule)
        return schedule
    }

    suspend fun getSchedules(): List<Schedule> {
        return collection.find().toList()
    }

    // Optional: Get by ID
    suspend fun getScheduleById(id: String): Schedule? {
        return collection.find(Schedule::id eq id).firstOrNull()
    }
}
