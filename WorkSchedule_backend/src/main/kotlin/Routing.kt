
package com.example.routes

import com.example.model.Schedule
import com.example.viewmodel.ScheduleViewModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.scheduleRoutes(viewModel: ScheduleViewModel) {
    route("/schedule") {

        post {
            val schedule = call.receive<Schedule>()
            val savedSchedule = viewModel.addSchedule(schedule)
            call.respond(HttpStatusCode.OK, savedSchedule)
        }

        get {
            val allSchedules = viewModel.getSchedules()
            call.respond(HttpStatusCode.OK, allSchedules)
        }

        get("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing or invalid ID"))
                return@get
            }

            if (!ObjectId.isValid(id)) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID format"))
                return@get
            }

            try {
                val schedule = viewModel.getScheduleById(id)
                if (schedule == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Schedule not found"))
                } else {
                    call.respond(HttpStatusCode.OK, schedule)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to fetch schedule"))
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing or invalid ID"))
                return@put
            }

            if (!ObjectId.isValid(id)) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID format"))
                return@put
            }

            try {
                val updatedSchedule = call.receive<Schedule>()
                val result = viewModel.updateSchedule(id, updatedSchedule)
                if (result == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Schedule not found"))
                } else {
                    call.respond(HttpStatusCode.OK, result)
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to update schedule"))
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing or invalid ID"))
                return@delete
            }

            if (!ObjectId.isValid(id)) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID format"))
                return@delete
            }

            try {
                val success = viewModel.deleteSchedule(id)
                if (success) {
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Schedule deleted successfully"))
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Schedule not found"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to delete schedule"))
            }
        }

        // CHECK EXISTS
        head("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank() || !ObjectId.isValid(id)) {
                call.respond(HttpStatusCode.BadRequest)
                return@head
            }

            try {
                val exists = viewModel.scheduleExists(id)
                call.respond(if (exists) HttpStatusCode.OK else HttpStatusCode.NotFound)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}