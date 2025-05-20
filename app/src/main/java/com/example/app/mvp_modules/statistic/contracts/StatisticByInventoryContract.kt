package com.example.app.mvp_modules.statistic.contracts

import com.example.app.dto.StatisticByInventory
import java.time.LocalDateTime

interface StatisticByInventoryContract {
    interface View{
        fun showDataRecycler(items: List<StatisticByInventory>, totalAmount: Double)
    }

    interface Presenter{
        fun statisticInventoryDateToDate(dateStart: LocalDateTime, dateEnd: LocalDateTime)
    }
}