package com.example.routing

import com.example.controller.ChatController
import com.example.models.ChatSocketMessage
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

fun Application.configureChatRoutes() {
    val controller = ChatController()
    val chatServer = ChatServer(controller)

    routing {
        webSocket("/ws/chat") {
            val sessionId = call.request.origin.remoteHost + ":" + chatServer.getNextId()
            val clientIp = call.request.origin.remoteHost

            println("새 연결: $sessionId")

            try {
                chatServer.addConnection(sessionId, this)

                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val json = Json { ignoreUnknownKeys = true }
                        val raw = frame.readText()
                        val socketMessage = json.decodeFromString<ChatSocketMessage>(raw)

                        val enrichedMessage = socketMessage.copy(ip = clientIp)
                        controller.save(enrichedMessage, clientIp)

                        chatServer.broadcast(enrichedMessage)
                    }
                }
            } catch (e: Exception) {
                println("WebSocket 에러: ${e.message}")
            } finally {
                chatServer.removeConnection(sessionId)
                println("연결 종료: $sessionId")
            }
        }
    }
}

class ChatServer(private val controller: ChatController) {
    private val connections = ConcurrentHashMap<String, WebSocketSession>()
    private val nextId = AtomicInteger(0)
    private val mutex = Mutex()

    fun getNextId(): Int = nextId.getAndIncrement()

    suspend fun broadcast(message: ChatSocketMessage) {
        val enrichedMessage = message.copy(ip = message.ip ?: "unknown")
        val jsonMessage = Json.encodeToString(enrichedMessage)

        connections.values.forEach { session ->
            try {
                session.send(Frame.Text(jsonMessage))
            } catch (e: Exception) {
                println("전송 실패: ${e.message}")
            }
        }
    }

    suspend fun addConnection(id: String, session: WebSocketSession) {
        mutex.withLock {
            connections[id] = session
            val history = controller.getAll()
            history.forEach {
                session.send(Frame.Text(Json.encodeToString(it)))
            }
        }
    }

    fun removeConnection(id: String) {
        connections.remove(id)
    }
}
