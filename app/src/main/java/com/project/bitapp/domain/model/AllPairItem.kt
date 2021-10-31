package com.project.bitapp.domain.model

data class AllPairItem(
    val symbol: String,
    val bid: Double,
    val bidSize: Double,
    val ask: Double,
    val askSize: Double,
    val dailyChange: Double,
    val dailyChangeRelative: Double,
    val dailyChangePercentage: String,
    val lastPrice: String,
    val volume: Double,
    val high: Double,
    val low: Double,
    val name: String,
)

