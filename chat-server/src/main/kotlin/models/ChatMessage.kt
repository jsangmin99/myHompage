package com.example.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class ChatMessage(
    @Contextual
    @BsonId val id: ObjectId = ObjectId(),
    val serverIp: String,  // 서버에서 감지한 IP
    val clientIp: String?, // 클라이언트에서 보낸 IP (선택적)
    val content: String,
    val timestamp: Long
)