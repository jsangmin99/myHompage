package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageRequest(
    val type: String,
    val content: String,
    val ip: String? = null,
    val timestamp: Long? = null
)
