package com.example.model

import com.example.serialization.ObjectIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Schedule(
    @BsonId
    @SerialName("_id")
    @Serializable(with = ObjectIdSerializer::class)
    val id: ObjectId? = null, // null để MongoDB auto-generate

    val title: String,
    val description: String,
    val selectedDays: Set<String>,
    val endDate: String? = null,
    val time: String? = null,
    val audioUrl: String? = null,
) {
    // Helper property để get id as String (cho API responses)
    val idString: String?
        get() = id?.toHexString()
}