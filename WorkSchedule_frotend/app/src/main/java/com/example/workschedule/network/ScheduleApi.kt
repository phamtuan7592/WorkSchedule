package com.example.workschedule.network


import com.example.workschedule.model.Schedule
import io.ktor.client.call.body
import io.ktor.client.request.*

import io.ktor.client.statement.bodyAsText
import io.ktor.http.*


object ScheduleApi {
    suspend fun postSchedule(request: Schedule): Boolean {
        return try {
            val response = NetworkClient.httpClient.post("http://10.0.2.2:8080/schedule") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            println("Response: ${response.bodyAsText()}")
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getSchedules(): List<Schedule> {
        return try {
            val response = NetworkClient.httpClient.get("http://10.0.2.2:8080/schedule")
            response.body()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getScheduleById(id: String): Schedule? {
        return try {
            val response = NetworkClient.httpClient.get("http://10.0.2.2:8080/schedule/$id")
            if (response.status == HttpStatusCode.OK) {
                response.body<Schedule>()
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    suspend fun updateSchedule(id: String, schedule: Schedule): Schedule? {
        return try {
            val response = NetworkClient.httpClient.put("http://10.0.2.2:8080/schedule/$id") {
                contentType(ContentType.Application.Json)
                setBody(schedule)
            }
            if (response.status == HttpStatusCode.OK) {
                response.body<Schedule>()  // lấy object trả về từ BE
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    suspend fun deleteSchedule(id: String): Boolean {
        return try {
            val response = NetworkClient.httpClient.delete("http://10.0.2.2:8080/schedule/$id")
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}