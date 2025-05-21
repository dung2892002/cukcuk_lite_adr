package com.example.app.datas.repositories

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.example.app.datas.CukcukDbHelper
import com.example.app.entities.Unit
import com.example.app.utils.getBoolean
import com.example.app.utils.getDateTime
import com.example.app.utils.getString
import com.example.app.utils.getUUID
import java.util.UUID

class UnitRepository(private val dbHelper: CukcukDbHelper) {
    private val db = dbHelper.readableDatabase

    @SuppressLint("Recycle")
    fun getAllUnit() : MutableList<Unit> {
        val units = mutableListOf<Unit>()

        val query = """
            SELECT u.UnitID, u.UnitName, u.Inactive FROM Unit u
        """.trimIndent()
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(query, null)
            while (cursor.moveToNext()) {
                val unit = Unit(
                    UnitID = cursor.getUUID("UnitID"),
                    UnitName = cursor.getString("UnitName"),
                    Description = "",
                    Inactive = cursor.getBoolean("Inactive"),
                    CreatedBy = "",
                    ModifiedBy = "",
                    CreatedDate = null,
                    ModifiedDate = null
                )
                units.add(unit)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
        finally {
            cursor?.close()
        }
        return units
    }

    fun createUnit(unit: Unit): Boolean {
        return try {
            val values = ContentValues().apply {
                put("UnitID", unit.UnitID?.toString() ?: UUID.randomUUID().toString())
                put("UnitName", unit.UnitName)
                put("Description", unit.Description)
                put("Inactive", if (unit.Inactive) 1 else 0)
                put("CreatedBy", unit.CreatedBy)
                put("ModifiedBy", unit.ModifiedBy)
                put("CreatedDate", unit.CreatedDate.toString())
                put("ModifiedDate", unit.ModifiedDate.toString())
            }

            val result = db.insert("Unit", null, values)
            result != -1L
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
            false
        }
    }

    fun updateUnit(unit: Unit): Boolean {
        if (unit.UnitID == null) return false

        return try {
            val values = ContentValues().apply {
                put("UnitName", unit.UnitName)
                put("Description", unit.Description)
                put("Inactive", if (unit.Inactive) 1 else 0)
                put("ModifiedBy", unit.ModifiedBy)
                put("ModifiedDate", unit.ModifiedDate.toString())
            }

            val result = db.update(
                "Unit",
                values,
                "UnitID = ?",
                arrayOf(unit.UnitID.toString())
            )
            result > 0
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
            false
        }
    }

}