package com.openclassroom.eventorias.core.utils

fun String.toFormattedAddress() : String {
    val parts = split(",").map { it.trim() }

    return if (parts.size >= 3) {
        """
        ${parts[0]},
        ${parts[1]},
        ${parts.drop(2).joinToString(", ")}
        """.trimIndent()
    } else {
        this
    }
}
