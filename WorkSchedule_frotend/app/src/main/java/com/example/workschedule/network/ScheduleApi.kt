package com.example.workschedule.network

import com.example.workschedule.model.Schedule
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
}