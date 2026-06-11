package com.openclassroom.eventorias.core.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

val dateFormatter : DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy").withLocale(Locale.ENGLISH)

val timeFormatter : DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
fun LocalDateTime.toFormattedDateString(): String {
    return this.format(dateFormatter)
}

fun LocalDate.toFormattedDateString(): String {
    return this.format(dateFormatter)
}

fun LocalDateTime.toFormattedTimeString(): String {
    return this.format(timeFormatter)
}
