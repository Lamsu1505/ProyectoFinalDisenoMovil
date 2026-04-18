package com.example.proyectofinaldisenomovil.core.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {

    private val dateFormatter = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es", "CO"))
    private val timeFormatter = SimpleDateFormat("h:mm a", Locale("es", "CO"))
    private val fullDateFormatter = SimpleDateFormat("dd 'de' MMMM 'del' yyyy", Locale("es", "CO"))
    private val shortDateFormatter = SimpleDateFormat("dd MMM", Locale("es", "CO"))

    fun formatDate(timestamp: Timestamp?): String {
        return timestamp?.let { dateFormatter.format(it.toDate()) } ?: ""
    }

    fun formatTime(timestamp: Timestamp?): String {
        return timestamp?.let { timeFormatter.format(it.toDate()) } ?: ""
    }

    fun formatFullDate(timestamp: Timestamp?): String {
        return timestamp?.let { fullDateFormatter.format(it.toDate()) } ?: ""
    }

    fun formatShortDate(timestamp: Timestamp?): String {
        return timestamp?.let { shortDateFormatter.format(it.toDate()) } ?: ""
    }

    fun formatDateTime(timestamp: Timestamp?): String {
        if (timestamp == null) return ""
        return "${formatDate(timestamp)} a las ${formatTime(timestamp)}"
    }

    fun toDate(timestamp: Timestamp?): Date? {
        return timestamp?.toDate()
    }

    fun toMillis(timestamp: Timestamp?): Long? {
        return timestamp?.toDate()?.time
    }

    fun fromMillis(millis: Long): Timestamp {
        return Timestamp(Date(millis))
    }

    fun fromDate(date: Date): Timestamp {
        return Timestamp(date)
    }
}

fun Timestamp?.toDisplayDate(): String = DateFormatter.formatDate(this)
fun Timestamp?.toDisplayTime(): String = DateFormatter.formatTime(this)
fun Timestamp?.toDisplayFullDate(): String = DateFormatter.formatFullDate(this)
fun Timestamp?.toDisplayShortDate(): String = DateFormatter.formatShortDate(this)
fun Timestamp?.toDisplayDateTime(): String = DateFormatter.formatDateTime(this)