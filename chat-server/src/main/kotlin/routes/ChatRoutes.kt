package com.example.routes

import com.example.controller.ChatController
import com.example.models.ChatMessageRequest
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import java.time.Instant
import java.util.*

fun Route.chatRoutes() {
    val controller = ChatController()
    val connections = Collections.synchronizedSet<Connection>(LinkedHashSet())

    route("/chat") {
        post {
            val body = call.receive<ChatMessageRequest>()
            val serverIp = call.request.origin.remoteHost
            controller.saveMessage(serverIp, body)
            call.respond(mapOf("status" to "success", "message" to "Saved!"))
        }

        get {
            val messages = controller.getAllMessages()
            call.respond(messages)
        }

        get("/server/{ip}") {
            val ip = call.parameters["ip"] ?: return@get call.respond(mapOf("error" to "No IP provided"))
            val messages = controller.getMessagesByServerIp(ip)
            call.respond(messages)
        }

        get("/client/{ip}") {
            val ip = call.parameters["ip"] ?: return@get call.respond(mapOf("error" to "No IP provided"))
            val messages = controller.getMessagesByClientIp(ip)
            call.respond(messages)
        }

        webSocket("/ws") {
            val connection = Connection(this)
            connections += connection

            try {
                send("연결되었습니다. 현재 ${connections.size}명이 접속 중입니다.")

                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val message = Json.decodeFromString<ChatMessageRequest>(receivedText)
                    
                    val serverIp = call.request.origin.remoteHost
                    controller.saveMessage(serverIp, message)

                    connections.forEach {
                        it.session.send("${message.ip ?: serverIp}: ${message.content}")
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                connections -= connection
            }
        }
    }
}

data class Connection(val session: DefaultWebSocketSession)
