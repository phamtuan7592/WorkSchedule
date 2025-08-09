package com.example.workschedule.model

import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    val id: String = "",
    val title: String,
    val description: String,
    val selectedDays: Set<String>,
    val endDate: String? = null,
    val time: String? = null
)
