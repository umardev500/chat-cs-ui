package com.umar.chat.utils

import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun formatEpochTime(epochSeonds: Long): String {
    val instant = Instant.ofEpochSecond(epochSeonds)
    val zoneId = ZoneId.systemDefault()
    val dateTime = instant.atZone(zoneId)

    val now = ZonedDateTime.now(zoneId).toLocalDate()
    val yesterday = now.minusDays(1)
    val startOfWeek = now.with(DayOfWeek.MONDAY)
    val localDate = dateTime.toLocalDate()
    val hourPattern = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

    return when {
        localDate == now -> hourPattern
        localDate == yesterday -> "Yesterday $hourPattern"
        localDate.isAfter(startOfWeek) -> dateTime.format(DateTimeFormatter.ofPattern("E HH:mm")) // Same week

        else -> dateTime.format(DateTimeFormatter.ofPattern("M/d/yyyy")) // Older than a week
    }
}