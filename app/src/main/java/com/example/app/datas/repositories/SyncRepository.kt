package com.example.app.datas.repositories

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.example.app.datas.CukcukDbHelper
import java.time.LocalDateTime
import java.util.UUID

class SyncRepository(dbHelper: CukcukDbHelper) {
    private val db = dbHelper.readableDatabase

    @SuppressLint("NewApi")
    fun create(tableName: String, objectId: UUID, action: Int) {
        try {
            val values = ContentValues().apply {
                put("SynchronizeID", UUID.randomUUID().toString())
                put("TableName", tableName)
                put("ObjectID", objectId.toString())
                put("Action", action)
                put("CreatedDate", LocalDateTime.now().toString())
            }
            db.insert("SynchronizeData", null, values)
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
    }

    fun delete(syncId: UUID) {
        try {
            val result = db.delete(
                "SynchronizeData",
                "SynchronizeID = ?",
                arrayOf(syncId.toString())
            )
            result > 0
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
            false
        }
    }

    fun deleteDataBeforeCreateDeleteSync(tableName: String, objectId: UUID) {
        try {
            val result = db.delete(
                "SynchronizeData",
                "TableName = ? AND ObjectID = ?",
                arrayOf(tableName, objectId.toString())
            )
            result > 0
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
            false
        }
    }

    fun getExistingSyncIdForCreateNew(tableName: String, objectId: UUID): UUID? {
        val query = """
            SELECT s.SynchronizeID FROM SynchronizeData s
            WHERE s.TableName = ? AND s.ObjectID = ? AND s."Action" = 0
            LIMIT 1
        """.trimIndent()

        var syncId: UUID? = null
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(query, arrayOf(tableName, objectId.toString()))
            if (cursor.moveToFirst()) {
                val idString = cursor.getString(cursor.getColumnIndexOrThrow("SynchronizeID"))
                syncId = UUID.fromString(idString)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            cursor?.close()
        }

        return syncId
    }

    fun getExistingSyncIdForCreateNewOrUpdate(tableName: String, objectId: UUID): UUID? {
        val query = """
            SELECT s.SynchronizeID FROM SynchronizeData s
            WHERE s.TableName = ? AND s.ObjectID = ? AND (s."Action" = 0 OR s."Action" = 1)
            LIMIT 1
        """.trimIndent()

        var syncId: UUID? = null
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(query, arrayOf(tableName, objectId.toString()))
            if (cursor.moveToFirst()) {
                val idString = cursor.getString(cursor.getColumnIndexOrThrow("SynchronizeID"))
                syncId = UUID.fromString(idString)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            cursor?.close()
        }

        return syncId
    }
}