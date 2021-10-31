package com.project.bitapp.utils

import com.android.bitapp.utils.interfaces.getDate
import com.project.bitapp.domain.model.AllPairItem
import com.project.bitapp.domain.model.TickerChannel
import com.project.bitapp.domain.model.Trade

fun List<String>.toAllPairItem(): AllPairItem {
    return AllPairItem(
        this[0],
        this[1].toDouble(),
        this[2].toDouble(),
        this[3].toDouble(),
        this[4].toDouble(),
        this[5].toDouble(),
        dailyChangeRelative = this[6].toDouble() * 100,
        formatTwoDecimal(this[6].toDouble() * 100) + "%",
        formatfourDecimal(this[7].toDouble()),
        this[8].toDouble(),
        this[9].toDouble(),
        this[10].toDouble(),
        extractName(this[0])
    )
}

fun List<Any>.toTickerData(): TickerChannel {
    val internalArray = this[1] as List<*>
    return TickerChannel(
        this[0] as Double,
        formatThreeDecimal(internalArray[0] as Double),
        formatThreeDecimal(internalArray[1] as Double),
        formatThreeDecimal(internalArray[2] as Double),
        formatThreeDecimal(internalArray[3] as Double),
        formatThreeDecimal(internalArray[4] as Double),
        formatThreeDecimal(internalArray[5] as Double * 100),
        formatThreeDecimal(internalArray[6] as Double),
        formatThreeDecimal(internalArray[7] as Double),
        formatThreeDecimal(internalArray[8] as Double),
        formatThreeDecimal(internalArray[9] as Double),
    )
}

fun List<Any>.toTradeData(): Trade {
    val internalArray = this[2] as List<*>
    return Trade(
        this[0] as Double,
        formatThreeDecimal(internalArray[0] as Double),
        getDate((internalArray[1] as Double).toLong()),
        formatThreeDecimal(internalArray[2] as Double),
        formatThreeDecimal(internalArray[3] as Double)
    )
}
