package com.example.app.datas.repositories

import com.example.app.datas.CukcukDbHelper

class StatisticRepository(private val dbHelper: CukcukDbHelper) {
    val db = dbHelper.readableDatabase
}