package com.openclassroom.eventorias.core.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

val dateFormatter : DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy").withLocale(Locale.ENGLISH)

fun LocalDateTime.toFormattedString(): String {
    return this.format(dateFormatter)
}

fun LocalDate.toFormattedString(): String {
    return this.format(dateFormatter)
}