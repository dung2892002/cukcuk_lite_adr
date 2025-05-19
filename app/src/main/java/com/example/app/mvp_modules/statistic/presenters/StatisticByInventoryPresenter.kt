package com.example.app.mvp_modules.statistic.presenters

import com.example.app.datas.repositories.StatisticRepository
import com.example.app.mvp_modules.statistic.contracts.StatisticByInventoryContract
import java.time.LocalDateTime

class StatisticByInventoryPresenter(
    private val view: StatisticByInventoryContract.View,
    private val repository: StatisticRepository
) : StatisticByInventoryContract.Presenter {
    override fun statisticInventoryDateToDate(
        dateStart: LocalDateTime,
        dateEnd: LocalDateTime,
    ) {
        val items = repository.getStatisticByInventory(dateStart, dateEnd)
        view.showDataRecycler(items)
    }
}