package com.example

import com.example.repository.ScheduleRepository
import com.example.routes.scheduleRoutes
import com.example.viewmodel.ScheduleViewModel
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    // Configure JSON serialization
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    // Configure routing
    configureRouting()
}

fun Application.configureRouting() {
    val repository = ScheduleRepository()
    val viewModel = ScheduleViewModel(repository)

    routing {
        get("/") {
            call.respondText("Schedule API is running!", ContentType.Text.Plain)
        }

        get("/health") {
            call.respond(mapOf("status" to "OK", "service" to "schedule-api"))
        }

        scheduleRoutes(viewModel)
    }
}