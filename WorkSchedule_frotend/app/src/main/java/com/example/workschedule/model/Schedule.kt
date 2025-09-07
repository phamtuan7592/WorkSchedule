package com.example.workschedule.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    @SerialName("_id")
    val id: String = "",
    val title: String,
    val description: String,
    val selectedDays: Set<String>,
    val endDate: String? = null,
    val time: String? = null,
    val audioUrl: String? = null,
    ){
    fun hasAudio(): Boolean {
        return !audioUrl.isNullOrEmpty()
    }
}
