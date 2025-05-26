package com.example.app.utils

import android.annotation.SuppressLint
import android.database.Cursor
import androidx.core.database.getDoubleOrNull
import java.time.LocalDateTime
import java.util.UUID

fun Cursor.getUUID(column: String): UUID =
    UUID.fromString(getString(getColumnIndexOrThrow(column)))

fun Cursor.getString(column: String): String =
    getString(getColumnIndexOrThrow(column)) ?: ""

fun Cursor.getBoolean(column: String): Boolean =
    getInt(getColumnIndexOrThrow(column)) != 0

fun Cursor.getDateTime(column: String) : LocalDateTime =
    DateTimeHelper.parseToLocalDateTimeOrNow(getString(getColumnIndexOrThrow(column)))

fun Cursor.getInt(column: String) : Int =
    getInt(getColumnIndexOrThrow(column))

fun Cursor.getDouble(column: String) : Double =
    getDoubleOrNull(getColumnIndexOrThrow(column))?:0.0

@SuppressLint("NewApi")
fun Cursor.getDateTimeOrNull(columnName: String): LocalDateTime? {
    val index = getColumnIndexOrThrow(columnName)
    if (isNull(index)) return null
    val value = getString(index)
    return value?.let { LocalDateTime.parse(it) }
}

