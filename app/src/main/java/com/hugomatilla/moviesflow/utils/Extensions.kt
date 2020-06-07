package com.hugomatilla.moviesflow.utils

import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT_TEXT = "yyyy-MM-dd"
fun Date.toText() = SimpleDateFormat(DATE_FORMAT_TEXT, Locale.getDefault()).format(this) ?: "Unknown"

fun Date.before(time: Int, type: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(type, -time)
    return calendar.time
}

fun Date.after(time: Int, type: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(type, time)
    return calendar.time
}

val now get() = Date()
val tenSecondsAgo get() = now.before(10, Calendar.SECOND)
val oneWeekAgo get() = now.before(7, Calendar.DAY_OF_YEAR)
val oneMonth get() = now.before(30, Calendar.DAY_OF_YEAR)
val tomorrow get() = now.after(1, Calendar.DAY_OF_YEAR)
