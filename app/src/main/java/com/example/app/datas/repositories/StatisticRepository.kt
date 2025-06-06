package com.example.app.datas.repositories

import android.annotation.SuppressLint
import android.database.Cursor
import com.example.app.datas.CukcukDbHelper
import com.example.app.dto.StatisticByInventory
import com.example.app.dto.StatisticByTime
import com.example.app.dto.StatisticOverview
import com.example.app.utils.DateTimeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

@SuppressLint("Recycle, NewApi")
class StatisticRepository(private val dbHelper: CukcukDbHelper) {
    suspend fun getStatisticOverview(): List<StatisticOverview> = withContext(Dispatchers.IO){
        val db = dbHelper.readableDatabase
        val result = mutableListOf<StatisticOverview>()
        val query = """
            SELECT 
                SUM(CASE WHEN date(InvoiceDate) = date('now', '-1 day') THEN Amount ELSE 0 END) AS Yesterday,
                SUM(CASE WHEN date(InvoiceDate) = date('now') THEN Amount ELSE 0 END) AS Today,
                SUM(CASE 
                    WHEN strftime('%W', InvoiceDate) = strftime('%W', 'now') 
                     AND strftime('%Y', InvoiceDate) = strftime('%Y', 'now')
                    THEN Amount ELSE 0 END) AS ThisWeek,
                SUM(CASE 
                    WHEN strftime('%m', InvoiceDate) = strftime('%m', 'now') 
                     AND strftime('%Y', InvoiceDate) = strftime('%Y', 'now') 
                    THEN Amount ELSE 0 END) AS ThisMonth,
                SUM(CASE 
                    WHEN strftime('%Y', InvoiceDate) = strftime('%Y', 'now') 
                    THEN Amount ELSE 0 END) AS ThisYear
            FROM Invoice
            WHERE PaymentStatus = 1
        """.trimIndent()

        var colors = listOf<String>("#5AB4FD", "#5AB4FD", "#4CAF50", "#F44336", "#2196F3")
        var icons = listOf<String>("ic-calendar-1.png", "ic-calendar-1.png", "ic-calendar-7.png", "ic-calendar-30.png", "ic-calendar-12.png")

        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(query, null)
            if (cursor.moveToFirst()) {
                val yesterday = cursor.getDouble(0)
                val today = cursor.getDouble(1)
                val thisWeek = cursor.getDouble(2)
                val thisMonth = cursor.getDouble(3)
                val thisYear = cursor.getDouble(4)

                val now = LocalDateTime.now()

                result.add(StatisticOverview("Hôm qua", yesterday,
                    DateTimeHelper.getStartOfDay(now.minusDays(1)),
                    DateTimeHelper.getEndOfDay(now.minusDays(1)),
                    colors[0], icons[0]
                ))
                result.add(StatisticOverview("Hôm nay", today,
                    DateTimeHelper.getStartOfDay(now),
                    DateTimeHelper.getEndOfDay(now),
                    colors[1], icons[1]
                ))
                result.add(StatisticOverview("Tuần này", thisWeek,
                    DateTimeHelper.getStartOfDay(now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))),
                    DateTimeHelper.getEndOfDay(now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))),
                    colors[2], icons[2]
                ))
                result.add(StatisticOverview("Tháng này", thisMonth,
                    DateTimeHelper.getStartOfDay(now.withDayOfMonth(1)),
                    DateTimeHelper.getEndOfDay(now.with(TemporalAdjusters.lastDayOfMonth())),
                    colors[3], icons[3]
                ))
                result.add(StatisticOverview("Năm nay", thisYear,
                    DateTimeHelper.getStartOfDay(now.withDayOfYear(1)),
                    DateTimeHelper.getEndOfDay(now.with(TemporalAdjusters.lastDayOfYear())),
                    colors[4], icons[4]
                ))
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
        finally {
            cursor?.close()
        }

        result
    }


    suspend fun getDailyStatisticOfWeek(startOfWeek: LocalDateTime, endOfWeek: LocalDateTime): List<StatisticByTime> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        var totalAmount = 0.0
        val query = """
            SELECT
                 date(InvoiceDate) as day,
                 SUM(Amount) as Amount
            FROM Invoice
            WHERE PaymentStatus = 1 AND date(InvoiceDate) BETWEEN ? AND ? 
            GROUP BY day
            ORDER BY day
        """.trimIndent()

        var cursor: Cursor? = null

        val resultMap = mutableMapOf<LocalDate, Double>()
        try {
            cursor = db.rawQuery(
                query,
                arrayOf(startOfWeek.toLocalDate().toString(), endOfWeek.toLocalDate().toString())
            )
            while (cursor.moveToNext()) {
                val day = LocalDate.parse(cursor.getString(0))
                val amount = cursor.getDouble(1)
                resultMap[day] = amount
                totalAmount += amount
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
        finally {
            cursor?.close()
        }

        val statistics = mutableListOf<StatisticByTime>()
        val daysBetween = ChronoUnit.DAYS.between(startOfWeek, endOfWeek)
        for (i in 0..daysBetween) {
            val day = startOfWeek.toLocalDate().plusDays(i.toLong())

            val title = when (day.dayOfWeek) {
                DayOfWeek.MONDAY -> "Thứ 2"
                DayOfWeek.TUESDAY -> "Thứ 3"
                DayOfWeek.WEDNESDAY -> "Thứ 4"
                DayOfWeek.THURSDAY -> "Thứ 5"
                DayOfWeek.FRIDAY -> "Thứ 6"
                DayOfWeek.SATURDAY -> "Thứ 7"
                DayOfWeek.SUNDAY -> "Chủ nhật"
            }

            statistics.add(
                StatisticByTime(
                    Title = title,
                    Amount = resultMap[day] ?: 0.0,
                    TimeStart = day.atStartOfDay(),
                    TimeEnd = day.atTime(LocalTime.MAX)
                )
            )
        }

        if (totalAmount == 0.0) statistics.clear()

        statistics
    }

    suspend fun getDailyStatisticOfMonth(start: LocalDateTime, end: LocalDateTime): List<StatisticByTime> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val startDate = start.toLocalDate()
        val endDate = end.toLocalDate()

        var totalAmount = 0.0
        val query = """
            SELECT
                 date(InvoiceDate) as day,
                 SUM(Amount) as Amount
            FROM Invoice
            WHERE PaymentStatus = 1 AND date(InvoiceDate) BETWEEN ? AND ?
            GROUP BY day
            ORDER BY day
        """.trimIndent()

        var cursor: Cursor? = null

        val resultMap = mutableMapOf<LocalDate, Double>()
        try {
            cursor = db.rawQuery(
                query,
                arrayOf(startDate.toString(), endDate.toString())
            )
            while (cursor.moveToNext()) {
                val day = LocalDate.parse(cursor.getString(0))
                val amount = cursor.getDouble(1)
                resultMap[day] = amount
                totalAmount += amount
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
        finally {
            cursor?.close()
        }

        val statistics = mutableListOf<StatisticByTime>()
        var currentDay = startDate
        while (!currentDay.isAfter(endDate)) {
            statistics.add(
                StatisticByTime(
                    Title = "Ngày ${currentDay.dayOfMonth}",
                    Amount = resultMap[currentDay] ?: 0.0,
                    TimeStart = currentDay.atStartOfDay(),
                    TimeEnd = currentDay.atTime(LocalTime.MAX)
                )
            )
            currentDay = currentDay.plusDays(1)
        }

        if (totalAmount == 0.0) statistics.clear()
        statistics
    }

    suspend fun getMonthlyStatistic(start: LocalDateTime, end: LocalDateTime): List<StatisticByTime> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val startDate = start.toLocalDate()
        val endDate = end.toLocalDate()
        var totalAmount = 0.0

        val query = """
            SELECT
                 strftime('%Y-%m', InvoiceDate) as month,
                 SUM(Amount) as Amount
            FROM Invoice
            WHERE PaymentStatus = 1 AND date(InvoiceDate) BETWEEN ? AND ?
            GROUP BY month
            ORDER BY month
        """.trimIndent()

        var cursor: Cursor? = null
        val resultMap = mutableMapOf<Pair<Int, Int>, Double>() // (year, month) -> amount
        try {
            cursor = db.rawQuery(
                query,
                arrayOf(startDate.toString(), endDate.toString())
            )
            while (cursor.moveToNext()) {
                val monthStr = cursor.getString(0) // e.g., "2024-05"
                val parts = monthStr.split("-")
                val year = parts[0].toInt()
                val month = parts[1].toInt()
                val amount = cursor.getDouble(1)
                resultMap[Pair(year, month)] = amount
                totalAmount += amount
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
        finally {
            cursor?.close()
        }

        val statistics = mutableListOf<StatisticByTime>()
        var current = startDate.withDayOfMonth(1)
        while (!current.isAfter(endDate)) {
            val firstDay = current.withDayOfMonth(1)
            val lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth())
            val key = Pair(firstDay.year, firstDay.monthValue)

            statistics.add(
                StatisticByTime(
                    Title = "Tháng ${firstDay.monthValue}",
                    Amount = resultMap[key] ?: 0.0,
                    TimeStart = firstDay.atStartOfDay(),
                    TimeEnd = lastDay.atTime(LocalTime.MAX)
                )
            )

            current = current.plusMonths(1)
        }

        if (totalAmount == 0.0) statistics.clear()
        statistics
    }

    suspend fun getStatisticByInventory(fromDate: LocalDateTime, toDate: LocalDateTime): List<StatisticByInventory> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val query = """
            SELECT
                d.InventoryName,
                SUM(d.Quantity) as TotalQuantity,
                SUM(d.Amount) as TotalAmount,
                d.UnitName
            FROM Invoice i
            INNER JOIN InvoiceDetail d ON i.InvoiceID = d.InvoiceID
            WHERE PaymentStatus = 1 AND date(i.InvoiceDate) BETWEEN ? AND ?
            GROUP BY d.InventoryID, d.InventoryName, d.UnitName
            ORDER BY TotalAmount DESC
        """.trimIndent()


        var colors = listOf<String>("#2196F3", "#4CAF50", "#F44336", "#FFC107", "#4B3FB5", "#001F54", "#BCBCBC")
        val list = mutableListOf<StatisticByInventory>()
        var totalAmount = 0.0
        var sortOrder = 1

        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(
                query,
                arrayOf(fromDate.toLocalDate().toString(), toDate.toLocalDate().toString())
            )
            while (cursor.moveToNext()) {
                val name = cursor.getString(0)
                val quantity = cursor.getDouble(1)
                val amount = cursor.getDouble(2)
                val unit = cursor.getString(3)
                totalAmount += amount

                list.add(
                    StatisticByInventory(
                        InventoryName = name,
                        Quantity = quantity,
                        Amount = amount,
                        UnitName = unit,
                        Percentage = 0.0,
                        Color = if (sortOrder > colors.size - 1) colors[colors.size - 1] else colors[sortOrder - 1],
                        SortOrder = sortOrder++
                    )
                )
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
        finally {
            cursor?.close()
        }

        if (totalAmount == 0.0) {
            list.clear()
            list
        }

        list.forEach {
            it.Percentage = if (totalAmount > 0) (it.Amount / totalAmount * 100.0) else 0.0
        }

        list
    }
}