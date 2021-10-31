package com.project.bitapp.domain.model

data class SubscribeRequest(
    val event: String,
    val channel: String,
    val symbol: String
)
