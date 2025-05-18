package com.example.app.datas.repositories

import android.annotation.SuppressLint
import com.example.app.datas.CukcukDbHelper
import com.example.app.dto.StatisticByInventory
import com.example.app.dto.StatisticByTime
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@SuppressLint("Recycle, NewApi")
class StatisticRepository(private val dbHelper: CukcukDbHelper) {
    val db = dbHelper.readableDatabase!!

    fun getStatisticOverview(): List<Double> {
        val result = mutableListOf<Double>()
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

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val yesterday = cursor.getDouble(0)
            val today = cursor.getDouble(1)
            val thisWeek = cursor.getDouble(2)
            val thisMonth = cursor.getDouble(3)
            val thisYear = cursor.getDouble(4)
            result.add(yesterday)
            result.add(today)
            result.add(thisWeek)
            result.add(thisMonth)
            result.add(thisYear)
        }
        cursor.close()
        return result
    }


    fun getDailyStatisticOfWeek(startOfWeek: LocalDateTime, endOfWeek: LocalDateTime): List<StatisticByTime> {
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

        val cursor = db.rawQuery(
            query,
            arrayOf(startOfWeek.toLocalDate().toString(), endOfWeek.toLocalDate().toString())
        )

        val resultMap = mutableMapOf<LocalDate, Double>()
        while (cursor.moveToNext()) {
            val day = LocalDate.parse(cursor.getString(0))
            val amount = cursor.getDouble(1)
            resultMap[day] = amount
            totalAmount += amount
        }

        cursor.close()

        val statistics = mutableListOf<StatisticByTime>()
        for (i in 0..6) {
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

        return statistics
    }

    fun getDailyStatisticOfMonth(year: Int, month: Int): List<StatisticByTime> {
        val startDate = LocalDate.of(year, month, 1)
        val endDate = startDate.withDayOfMonth(startDate.lengthOfMonth())

        val query = """
            SELECT
                 date(InvoiceDate) as day,
                 SUM(Amount) as Amount
            FROM Invoice
            WHERE PaymentStatus = 1 AND date(InvoiceDate) BETWEEN ? AND ?
            GROUP BY day
            ORDER BY day
        """.trimIndent()

        val cursor = db.rawQuery(
            query,
            arrayOf(startDate.toString(), endDate.toString())
        )

        val resultMap = mutableMapOf<LocalDate, Double>()
        while (cursor.moveToNext()) {
            val day = LocalDate.parse(cursor.getString(0))
            val amount = cursor.getDouble(1)
            resultMap[day] = amount
        }

        cursor.close()

        val statistics = mutableListOf<StatisticByTime>()
        for (i in 1..startDate.lengthOfMonth()) {
            val day = LocalDate.of(year, month, i)
            statistics.add(
                StatisticByTime(
                    Title = "Ngày $i",
                    Amount = resultMap[day] ?: 0.0,
                    TimeStart = day.atStartOfDay(),
                    TimeEnd = day.atTime(LocalTime.MAX)
                )
            )
        }

        return statistics
    }


    fun getMonthlyStatisticOfYear(year: Int): List<StatisticByTime> {
        val startDate = LocalDate.of(year, 1, 1)
        val endDate = LocalDate.of(year, 12, 31)

        val query = """
            SELECT
                 strftime('%m', InvoiceDate) as month,
                 SUM(Amount) as Amount
            FROM Invoice
            WHERE PaymentStatus = 1 AND date(InvoiceDate) BETWEEN ? AND ?
            GROUP BY month
            ORDER BY month
        """.trimIndent()

        val cursor = db.rawQuery(
            query,
            arrayOf(startDate.toString(), endDate.toString())
        )

        val resultMap = mutableMapOf<Int, Double>()
        while (cursor.moveToNext()) {
            val month = cursor.getString(0).toInt()
            val amount = cursor.getDouble(1)
            resultMap[month] = amount
        }

        cursor.close()

        val statistics = mutableListOf<StatisticByTime>()
        for (month in 1..12) {
            val firstDay = LocalDate.of(year, month, 1)
            val lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth())

            statistics.add(
                StatisticByTime(
                    Title = "Tháng $month",
                    Amount = resultMap[month] ?: 0.0,
                    TimeStart = firstDay.atStartOfDay(),
                    TimeEnd = lastDay.atTime(LocalTime.MAX)
                )
            )
        }

        return statistics
    }


    fun getStatisticByInventory(fromDate: LocalDateTime, toDate: LocalDateTime): List<StatisticByInventory> {
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

        val cursor = db.rawQuery(
            query,
            arrayOf(fromDate.toLocalDate().toString(), toDate.toLocalDate().toString())
        )

        val list = mutableListOf<StatisticByInventory>()
        var totalRevenue = 0.0

        var sortOrder = 1
        while (cursor.moveToNext()) {
            val name = cursor.getString(0)
            val quantity = cursor.getDouble(1)
            val amount = cursor.getDouble(2)
            val unit = cursor.getString(3)
            totalRevenue += amount

            list.add(
                StatisticByInventory(
                    InventoryName = name,
                    Quantity = quantity,
                    Amount = amount,
                    UnitName = unit,
                    Percentage = 0.0,
                    SortOrder = sortOrder++
                )
            )
        }

        list.forEach {
            it.Percentage = if (totalRevenue > 0) (it.Amount / totalRevenue * 100.0) else 0.0
        }

        cursor.close()

        return list
    }


}