package com.project.bitapp.domain.model

data class TickerChannel(
    var channel: Double,
    var bid: String, // Price of last highest bid
    var bidSize: String,
    var ask: String, // Price of last lowest ask
    var askSize: String,
    var dailyChange: String, // Amount that the last price has changed since yesterday
    var dailyChangeRelative: String, // Relative price change since yesterday (*100 for percentage change)
    var openPrice: String,
    var volume: String,
    var high: String, // Daily high
    var low: String // Daily low
)
