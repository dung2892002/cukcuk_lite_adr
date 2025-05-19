package com.example.app.utils

import android.annotation.SuppressLint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.IsoFields

@SuppressLint("NewApi")
object DateTimeHelper {
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

    fun getPreviousWeek(): Pair<Int, Int> {
        val (year, week) = getCurrentWeek()
        return if (week > 1) {
            Pair(year, week - 1)
        } else {
            val prevYear = year - 1
            val lastWeek = getWeeksInYear(prevYear)
            Pair(prevYear, lastWeek)
        }
    }

    fun getWeeksInYear(year: Int): Int {
        val date = LocalDate.of(year, 12, 28)
        return date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
    }

    fun getStartAndEndOfWeek(year: Int, week: Int): Pair<LocalDateTime, LocalDateTime> {
        val startOfWeek = LocalDate
            .ofYearDay(year, 1)
            .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week.toLong())
            .with(ChronoField.DAY_OF_WEEK, 1) // Thứ Hai

        val endOfWeek = startOfWeek.plusDays(6)

        return Pair(startOfWeek.atStartOfDay(), endOfWeek.atTime(LocalTime.MAX))
    }

    fun getStartAndEndOfCurrentMonth(): Pair<LocalDateTime, LocalDateTime> {
        val now = LocalDate.now()
        val start = now.withDayOfMonth(1)
        val end = now.withDayOfMonth(now.lengthOfMonth())
        return Pair(start.atStartOfDay(), end.atTime(LocalTime.MAX))
    }

    fun getStartAndEndOfPreviousMonth(): Pair<LocalDateTime, LocalDateTime> {
        val now = LocalDate.now().minusMonths(1)
        val start = now.withDayOfMonth(1)
        val end = now.withDayOfMonth(now.lengthOfMonth())
        return Pair(start.atStartOfDay(), end.atTime(LocalTime.MAX))
    }

    // ✅ Năm nay
    fun getStartAndEndOfCurrentYear(): Pair<LocalDateTime, LocalDateTime> {
        val year = LocalDate.now().year
        val start = LocalDate.of(year, 1, 1)
        val end = LocalDate.of(year, 12, 31)
        return Pair(start.atStartOfDay(), end.atTime(LocalTime.MAX))
    }

    // ✅ Năm trước
    fun getStartAndEndOfPreviousYear(): Pair<LocalDateTime, LocalDateTime> {
        val year = LocalDate.now().year - 1
        val start = LocalDate.of(year, 1, 1)
        val end = LocalDate.of(year, 12, 31)
        return Pair(start.atStartOfDay(), end.atTime(LocalTime.MAX))
    }


    fun getStartOfDay(dateTime: LocalDateTime) : LocalDateTime {
        return dateTime.withHour(0).withMinute(0).withSecond(0).withNano(0)
    }

    fun getEndOfDay(dateTime: LocalDateTime) : LocalDateTime {
        return dateTime.withHour(23).withMinute(59).withSecond(59).withNano(0)
    }
}