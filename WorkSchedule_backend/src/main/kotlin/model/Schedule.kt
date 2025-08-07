package com.example.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Schedule(
    @BsonId val id: String = ObjectId().toString(),
    val title: String,
    val description: String,
    val selectedDays: Set<String>,
    val endDate: String? = null,
    val time: String? = null
)
