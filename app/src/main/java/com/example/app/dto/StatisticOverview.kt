package com.example.app.dto

import java.time.LocalDateTime

data class StatisticOverview (
    var Title: String,
    var Amount: Double,
    var TimeStart: LocalDateTime,
    var TimeEnd: LocalDateTime,
    var Color: String,
    var IconFile: String,
)