package com.example.app.utils

import android.annotation.SuppressLint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.IsoFields

@SuppressLint("NewApi")
object DateTimeParser {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun parseToLocalDateTimeOrNow(dateString: String?): LocalDateTime {
        return try {
            LocalDateTime.parse(dateString, formatter)
        } catch (e: Exception) {
            LocalDateTime.now()
        }
    }

    fun getCurrentWeek(): Pair<Int, Int> {
        val today = LocalDate.now()
        val week = today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
        val year = today.get(IsoFields.WEEK_BASED_YEAR)
        return Pair(year, week)
    }

    fun getStartAndEndOfWeek(year: Int, week: Int): Pair<LocalDateTime, LocalDateTime> {
        val startOfWeek = LocalDate
            .ofYearDay(year, 1)
            .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week.toLong())
            .with(ChronoField.DAY_OF_WEEK, 1) // Thứ Hai

        val endOfWeek = startOfWeek.plusDays(6)

        return Pair(startOfWeek.atStartOfDay(), endOfWeek.atTime(LocalTime.MAX))
    }


}