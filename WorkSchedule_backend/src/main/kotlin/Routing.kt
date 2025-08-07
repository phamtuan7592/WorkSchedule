package com.example

import com.example.model.*
import com.example.repository.ScheduleRepository
import com.example.viewmodel.ScheduleViewModel
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun Application.configureRouting() {
    val repository = ScheduleRepository()
    val viewModel = ScheduleViewModel(repository)
    routing {
        get("/") {
            call.respondText("hello")
        }

        scheduleRoutes(viewModel)
    }
}

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
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing ID")
                return@get
            }

            val schedule = viewModel.getScheduleById(id)
            if (schedule == null) {
                call.respond(HttpStatusCode.NotFound, "Schedule not found")
            } else {
                call.respond(HttpStatusCode.OK, schedule)
            }
        }
    }
}

