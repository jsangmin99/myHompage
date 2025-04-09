package com.example.plugins

import com.example.serializers.ObjectIdSerializer
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.bson.types.ObjectId
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

fun Application.configureSerialization() {
    val json = Json {
        prettyPrint = true
        isLenient = true
        serializersModule = SerializersModule {
            contextual(ObjectId::class, ObjectIdSerializer)
        }
    }

    install(ContentNegotiation) {
        json(json)
    }

    routing {
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
