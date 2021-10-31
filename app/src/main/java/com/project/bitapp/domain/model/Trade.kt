package com.project.bitapp.domain.model

data class Trade(
    var channel: Double,
    var id: String,
    var milliSecondStamp: String, // millisecond time stamp
    var amount: String, // Amount bought (positive) or sold (negative)
    var price: String, // Price at which the trade was executed

)
