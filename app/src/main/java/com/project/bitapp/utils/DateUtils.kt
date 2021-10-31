package com.android.bitapp.utils.interfaces

import java.text.SimpleDateFormat
import java.util.*

fun getDate(milliSeconds: Long): String {
    val dateFormat = SimpleDateFormat("hh.mm aa", Locale.getDefault())
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return dateFormat.format(calendar.time)
}


