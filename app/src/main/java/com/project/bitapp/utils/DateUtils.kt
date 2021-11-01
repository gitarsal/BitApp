package com.project.bitapp.utils

import java.text.SimpleDateFormat
import java.util.* // ktlint-disable no-wildcard-imports

fun getDate(milliSeconds: Long): String {
    val dateFormat = SimpleDateFormat("hh.mm aa", Locale.getDefault())
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return dateFormat.format(calendar.time)
}
