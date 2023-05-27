package com.podplay.android.util

import android.os.Build
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

object DateUtils {
    fun jsonDateToShortDate(jsonDate: String?): String {
        if (jsonDate == null) {
            return "-"
        }

        val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = inFormat.parse(jsonDate) ?: return "-"
        val outputFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
        return outputFormat.format(date)
    }

    fun xmlDateToDate(dateString: String?): Date {
        val date = dateString ?: return Date()
        val inFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault())
        return inFormat.parse(date) ?: Date()
    }

    fun dateToMonthDayYear(date: Date): String {
        val formatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    fun formatTimePassed(date: Date): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val instant = Instant.ofEpochMilli(date.time)
            val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            val currentDateTime = LocalDateTime.now()
            val duration = Duration.between(localDateTime, currentDateTime)
            when {
                duration.seconds < 60 -> "${duration.seconds} seconds ago"
                duration.toMinutes() < 60 -> "${duration.toMinutes()} minutes ago"
                duration.toHours() < 24 -> "${duration.toHours()} hours ago"
                duration.toDays() <= 7 -> "${duration.toDays()} days ago"
                else -> dateToMonthDayYear(date)
            }
        } else {
            dateToMonthDayYear(date)
        }
    }

//    fun timeToMinutes(timeString: String): String {
//        val parts = timeString.split(":")
//        val minutes = parts[0].toInt()
//        val seconds = parts[1].toInt()
//        val totalMinutes = minutes + (seconds.toFloat() / 60f)
//        val roundedMinutes = if (totalMinutes % 1 >= 0.5) {
//            totalMinutes.toInt() + 1
//        } else {
//            totalMinutes.toInt()
//        }
//        return if (roundedMinutes < 60) {
//            "$roundedMinutes min"
//        } else {
//            val hours = roundedMinutes / 60
//            val remainingMinutes = roundedMinutes % 60
//            "$hours hr $remainingMinutes min"
//        }
//    }


}
