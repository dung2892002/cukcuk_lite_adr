package com.example.app.dto

import java.time.LocalDateTime

data class StatisticByTime (
    var Title: String,
    var Amount: Double,
    var TimeStart: LocalDateTime,
    var TimeEnd: LocalDateTime
)