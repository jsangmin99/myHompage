package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatSocketMessage(
    val type: String,
    val content: String,
    val user: String,
    val timestamp: String,
    val ip: String? = null
)
