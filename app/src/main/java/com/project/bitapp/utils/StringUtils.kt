package com.project.bitapp.utils

import android.util.Log

fun extractName(pair: String?): String {
    if (pair.isNullOrBlank())
        return ""
    else {
        return if (pair.contains(":")) {
            val array = pair.split(":")
            if (array.size == 2) {
                array[0].removePrefix("t").plus(" / ").plus(array[1]).uppercase()
            } else {
                return pair
            }
        } else {
            if (pair.length == 7) {
                val firstName = pair.substring(0..3)
                val lastName = pair.substring(4..6)
                return firstName.removePrefix("t").plus(" / ").plus(lastName).uppercase()
            } else {
                return pair
            }
        }
    }
}

fun getAllPairsUrl(list: List<String>): String {
    var completeUrl = BASE_URL + GET_PAIRS_DETAIL_URL
    if (list.isNotEmpty()) {
        val commaSeparatedString = list.joinToString(separator = ",t", prefix = "t") { it }
        completeUrl += commaSeparatedString
    }
    Log.d("url", completeUrl)
    return completeUrl
}

fun formatTwoDecimal(value: Double?): String {
    return String.format("%.2f", value)
}

fun formatThreeDecimal(value: Double): String {
    return String.format("%.3f", value)
}

fun formatfourDecimal(value: Double): String {
    return String.format("%.4f", value)
}
