package com.example.controller

import com.example.models.ChatMessage
import com.example.models.ChatMessageRequest
import com.example.plugins.MongoProvider
import org.litote.kmongo.eq
import java.time.Instant

class ChatController {
    private val chatCollection = MongoProvider.database.getCollection<ChatMessage>()

    suspend fun saveMessage(serverIp: String, request: ChatMessageRequest) {
        val message = ChatMessage(
            serverIp = serverIp,
            clientIp = request.ip,
            content = request.content,
            timestamp = Instant.now().toEpochMilli()
        )
        chatCollection.insertOne(message)
    }

    suspend fun getAllMessages(): List<ChatMessage> {
        return chatCollection.find().toList()
    }

    suspend fun getMessagesByServerIp(ip: String): List<ChatMessage> {
        return chatCollection.find(ChatMessage::serverIp eq ip).toList()
    }

    suspend fun getMessagesByClientIp(ip: String): List<ChatMessage> {
        return chatCollection.find(ChatMessage::clientIp eq ip).toList()
    }
}
