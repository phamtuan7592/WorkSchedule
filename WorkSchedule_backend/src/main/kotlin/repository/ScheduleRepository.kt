// 3. Repository with proper error handling
package com.example.repository

import com.example.data.MongoDbClient
import com.example.model.Schedule
import com.mongodb.client.model.Filters
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory

class ScheduleRepository {
    private val collection = MongoDbClient.database.getCollection<Schedule>("schedules")
    private val logger = LoggerFactory.getLogger(ScheduleRepository::class.java)

    suspend fun addSchedule(schedule: Schedule): Schedule {
        collection.insertOne(schedule)
        return schedule
    }

    suspend fun getSchedules(): List<Schedule> {
        return collection.find().toList()
    }

    suspend fun getScheduleById(id: String): Schedule? =
        if (ObjectId.isValid(id)) {
            collection.find(Filters.eq("_id", ObjectId(id))).firstOrNull()
        } else null

    suspend fun updateSchedule(id: String, updated: Schedule): Schedule? =
        if (ObjectId.isValid(id)) {
            val objectId = ObjectId(id)
            val scheduleWithId = updated.copy(id = objectId)
            val result = collection.replaceOne(Filters.eq("_id", objectId), scheduleWithId)
            if (result.modifiedCount > 0) scheduleWithId else null
        } else null

    suspend fun deleteSchedule(id: String): Boolean =
        if (ObjectId.isValid(id)) {
            val result = collection.deleteOne(Filters.eq("_id", ObjectId(id)))
            result.deletedCount > 0
        } else false

    suspend fun scheduleExists(id: String): Boolean =
        ObjectId.isValid(id) && collection.countDocuments(Filters.eq("_id", ObjectId(id))) > 0
}
