package com.example.app.datas

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CukcukDbHelper(context: Context) : SQLiteOpenHelper(context, "cukcuk.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int,
    ) {

    }
}