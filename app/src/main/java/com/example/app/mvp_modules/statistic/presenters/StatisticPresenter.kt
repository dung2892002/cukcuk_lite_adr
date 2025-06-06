package com.example.app.mvp_modules.statistic.presenters

import android.annotation.SuppressLint
import com.example.app.datas.repositories.StatisticRepository
import com.example.app.dto.StatisticByTime
import com.example.app.dto.StatisticOverview
import com.example.app.mvp_modules.statistic.contracts.StatisticContract
import com.example.app.utils.DateTimeHelper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
class StatisticPresenter (
    private val view: StatisticContract.View,
    private val repository: StatisticRepository) : StatisticContract.Presenter
{
    val weekDays = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
    val daysInMonth = (1..31).map { it.toString() }
    val months = listOf("T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12")

    override suspend fun statisticOverview() {
        val items = repository.getStatisticOverview()
        view.showStatisticOverview(items)
    }

    override suspend fun statisticCurrentWeek() {
        var (year, week) = DateTimeHelper.getCurrentWeek()
        var (start, end) = DateTimeHelper.getStartAndEndOfWeek(year, week)
        end = LocalDateTime.now()
        val items = repository.getDailyStatisticOfWeek(start, end)

        view.showStatisticByTime(items, "Tuần này", weekDays, 0)
    }

    override suspend fun statisticPreviousWeek() {
        val (year, week) = DateTimeHelper.getPreviousWeek()
        val (start, end) = DateTimeHelper.getStartAndEndOfWeek(year, week)

        val items = repository.getDailyStatisticOfWeek(start, end)

        view.showStatisticByTime(items, "Tuần trước", weekDays, 0)
    }

    override suspend fun statisticCurrentMonth() {
        var (start, end) = DateTimeHelper.getStartAndEndOfCurrentMonth()
        end = LocalDateTime.now()

        val items = repository.getDailyStatisticOfMonth(start, end)

        view.showStatisticByTime(items, "Tháng này", daysInMonth, 1)
    }

    override suspend fun statisticPreviousMonth() {
        val (start, end) = DateTimeHelper.getStartAndEndOfPreviousMonth()

        val items = repository.getDailyStatisticOfMonth(start, end)

        view.showStatisticByTime(items, "Tháng trước", daysInMonth, 1)
    }

    override suspend fun statisticCurrentYear() {
        var (start, end) = DateTimeHelper.getStartAndEndOfCurrentYear()
        end = LocalDateTime.now()


        val items = repository.getMonthlyStatistic(start, end)

        view.showStatisticByTime(items, "Năm nay", months, 2)
    }

    override suspend fun statisticPreviousYear() {
        val (start, end) = DateTimeHelper.getStartAndEndOfPreviousYear()

        val items = repository.getMonthlyStatistic(start, end)

        view.showStatisticByTime(items, "Năm trước",months, 2)
    }

    override suspend fun statisticInventoryDateToDate(
        dateStart: LocalDateTime,
        dateEnd: LocalDateTime,
    ) {
        val items = repository.getStatisticByInventory(dateStart, dateEnd)
        var totalAmount = 0.0
        items.forEach { it -> totalAmount += it.Amount }
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val label = dateStart.toLocalDate().format(formatter) + " - " + dateEnd.toLocalDate().format(formatter)
        view.showStatisticByInventory(items,label, totalAmount)
    }

    override suspend fun handleClickItemOverview(item: StatisticOverview,position: Int) {
        if (item.Amount == 0.0) return
        if (position == 2) {
            statisticCurrentWeek()
            return
        }
        if (position == 3) {
            statisticCurrentMonth()
            return
        }
        if (position == 4) {
            statisticCurrentYear()
            return
        }
        handleNavigateByOverview(item)
        return
    }

    override fun handleNavigateByTime(item: StatisticByTime) {
        if (item.Amount == 0.0) return

        var label = getLabel(item)

        view.navigateToStatisticInventoryActivity(item.TimeStart, item.TimeEnd, label)
    }

    override fun handleNavigateByOverview(item: StatisticOverview) {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val label = item.Title + " (" + item.TimeStart.toLocalDate().format(formatter) + ")"

        view.navigateToStatisticInventoryActivity(item.TimeStart, item.TimeEnd, label)
    }

    private fun getLabel(item: StatisticByTime) : String{
        var label = item.Title + " ("
        if (item.Title.startsWith("Ngày")) label = ""

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        label += if (item.TimeStart.toLocalDate() == item.TimeEnd.toLocalDate()) {
            item.TimeStart.toLocalDate().format(formatter)
        } else {
            item.TimeStart.toLocalDate().format(formatter) + " - " + item.TimeEnd.toLocalDate().format(formatter)
        }
        if (!item.Title.startsWith("Ngày")) label += ")"

        return label
    }
}