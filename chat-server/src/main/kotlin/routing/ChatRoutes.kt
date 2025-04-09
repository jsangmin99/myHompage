package com.example.routing

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Instant
import kotlinx.serialization.json.*
import kotlinx.serialization.Serializable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Serializable
data class ChatMessage(
    val content: String,
    val timestamp: String = Instant.now().toString(),
    val ip: String? = null,
    val isOwn: Boolean = false,
    val clientId: String? = null
)

class ChatServer {
    private val connections = ConcurrentHashMap<String, WebSocketSession>()
    private val messageHistory = mutableListOf<ChatMessage>()
    private val nextId = AtomicInteger(0)
    private val mutex = Mutex()

    fun getNextId(): Int {
        return nextId.getAndIncrement()
    }

    suspend fun broadcast(message: ChatMessage) {
        val jsonMessage = Json.encodeToString(message)
        connections.values.forEach { session ->
            try {
                session.send(Frame.Text(jsonMessage))
            } catch (e: Exception) {
                println("메시지 전송 실패: ${e.message}")
                // 연결이 끊어진 세션 제거
                connections.entries.removeIf { it.value == session }
            }
        }
    }

    suspend fun addConnection(id: String, session: WebSocketSession) {
        mutex.withLock {
            connections[id] = session
            // 새로 연결된 클라이언트에게 이전 메시지 전송
            messageHistory.forEach { message ->
                try {
                    session.send(Frame.Text(Json.encodeToString(message)))
                } catch (e: Exception) {
                    println("이전 메시지 전송 실패: ${e.message}")
                    throw e
                }
            }
        }
    }

    fun removeConnection(id: String) {
        connections.remove(id)
    }

    suspend fun addMessage(message: ChatMessage) {
        mutex.withLock {
            messageHistory.add(message)
            // 메시지 히스토리 크기 제한 (최근 100개만 유지)
            if (messageHistory.size > 100) {
                messageHistory.removeAt(0)
            }
        }
    }
}

fun Application.configureChatRoutes() {
    val chatServer = ChatServer()
    
    routing {
        webSocket("/chat") {
            val sessionId = "${call.request.origin.remoteHost}:${chatServer.getNextId()}"
            println("새로운 연결: $sessionId")
            
            try {
                chatServer.addConnection(sessionId, this)
                
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val message = frame.readText()
                        val chatMessage = Json.decodeFromString<ChatMessage>(message)
                        chatServer.addMessage(chatMessage)
                        chatServer.broadcast(chatMessage)
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