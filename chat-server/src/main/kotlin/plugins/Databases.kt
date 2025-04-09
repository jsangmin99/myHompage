package com.example.plugins

import io.ktor.server.application.*

fun Application.configureDatabases() {
    MongoProvider.init(environment.config)
}
