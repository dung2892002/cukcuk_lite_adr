package com.example.app.dto

data class StatisticByInventory (
    var InventoryName: String,
    var Quantity: Double,
    var Amount: Double,
    var UnitName: String,
    var Percentage: Double,
    var SortOrder: Int,
    var Color: String
)