package com.example.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class ChatEntity(
    @Contextual
    @BsonId val id: ObjectId = ObjectId(),
    val serverIp: String,
    val clientUser: String,
    val content: String,
    val timestamp: Long
)
