package com.example.app.dto

data class StatisticResult(
    val TotalAmount: Double,
    val statisticsByTime: List<StatisticByTime>,
    val statisticsByInventory: List<StatisticByInventory>
)