package com.project.bitapp.domain.model

data class MessageSubscribeSchema(
    var event: String,
    var channel: String,
    var chanId: Double,
    var symbol: String,
    var pair: String,
)
