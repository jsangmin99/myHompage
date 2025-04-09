package com.example

import com.example.plugins.*
import com.example.routing.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureDatabases() // 여기서 Mongo 연결
    configureChatRoutes()
}
