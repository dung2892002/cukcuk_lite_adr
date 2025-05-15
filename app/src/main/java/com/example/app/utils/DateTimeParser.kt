package com.example.app.utils

import android.annotation.SuppressLint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
object DateTimeParser {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun parseToLocalDateTimeOrNow(dateString: String?): LocalDateTime {
        return try {
            LocalDateTime.parse(dateString, formatter)
        } catch (e: Exception) {
            LocalDateTime.now()
        }
    }

}