package com.example.controller

import com.example.models.ChatEntity
import com.example.models.ChatSocketMessage
import com.example.plugins.MongoProvider
import kotlinx.coroutines.flow.toList
import java.time.Instant

class ChatController {
    private val chatCollection = MongoProvider.database.getCollection<ChatEntity>()

    suspend fun save(socketMessage: ChatSocketMessage, serverIp: String) {
        chatCollection.insertOne(
            ChatEntity(
                serverIp = serverIp,
                clientUser = socketMessage.user,
                content = socketMessage.content,
                timestamp = Instant.parse(socketMessage.timestamp).toEpochMilli()
            )
        )
    }

    suspend fun getAll(): List<ChatSocketMessage> {
        return chatCollection.find().toList().map {
            ChatSocketMessage(
                type = "chat",
                content = it.content,
                user = it.clientUser,
                timestamp = Instant.ofEpochMilli(it.timestamp).toString(),
                ip = it.clientUser
            )
        }
    }
}
