package com.example.app.mvp_modules.statistic.contracts

import com.example.app.dto.StatisticByInventory
import com.example.app.dto.StatisticByTime
import com.example.app.dto.StatisticOverview
import java.time.LocalDateTime

interface StatisticContract {
    interface View{
        fun showStatisticOverview(items: List<StatisticOverview>)
        fun showStatisticByTime(items: List<StatisticByTime>, label: String, xChartLabels: List<String>, type: Int) //type: 0 - tuan, 1- thang, 2- nam
        fun showStatisticByInventory(items: List<StatisticByInventory>, label: String, totalAmount: Double)
        fun navigateToStatisticInventoryActivity(start: LocalDateTime, end: LocalDateTime, label: String)
    }

    interface Presenter{
        suspend fun handleClickItemOverview(item: StatisticOverview, position: Int)
        fun handleNavigateByTime(item: StatisticByTime)
        fun handleNavigateByOverview(item: StatisticOverview)
        suspend fun statisticOverview()
        suspend fun statisticCurrentWeek()
        suspend fun statisticPreviousWeek()
        suspend fun statisticCurrentMonth()
        suspend fun statisticPreviousMonth()
        suspend fun statisticCurrentYear()
        suspend fun statisticPreviousYear()
        suspend fun statisticInventoryDateToDate(dateStart: LocalDateTime, dateEnd: LocalDateTime)
    }
}