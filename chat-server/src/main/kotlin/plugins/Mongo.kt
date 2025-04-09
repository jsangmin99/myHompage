package com.example.plugins

import io.ktor.server.config.*
import org.litote.kmongo.coroutine.*
import org.litote.kmongo.reactivestreams.KMongo

object MongoProvider {
    lateinit var database: CoroutineDatabase

    fun init(config: ApplicationConfig) {
        val uri = config.property("mongo.uri").getString()
        val dbName = config.property("mongo.db").getString()

        val client = KMongo.createClient(uri).coroutine
        database = client.getDatabase(dbName)
    }
}

