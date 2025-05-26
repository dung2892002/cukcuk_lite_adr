package com.example.app.datas.repositories

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.example.app.datas.CukcukDbHelper
import java.time.LocalDateTime
import java.util.UUID

@SuppressLint("NewApi")
class SyncRepository(dbHelper: CukcukDbHelper) {
    private val db = dbHelper.readableDatabase

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

    fun createRange(tableName: String, objectIds: List<UUID>, action: Int) {
        try {
            db.beginTransaction()
            for (objectId in objectIds) {
                val values = ContentValues().apply {
                    put("SynchronizeID", UUID.randomUUID().toString())
                    put("TableName", tableName)
                    put("ObjectID", objectId.toString())
                    put("Action", action)
                    put("CreatedDate", LocalDateTime.now().toString())
                }
                db.insert("SynchronizeData", null, values)
            }
            db.setTransactionSuccessful()
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        } finally {
            db.endTransaction()
        }
    }

    fun deleteRange(syncIds: List<UUID>) {
        try {
            db.beginTransaction()
            for (syncId in syncIds) {
                db.delete(
                    "SynchronizeData",
                    "SynchronizeID = ?",
                    arrayOf(syncId.toString())
                )
            }
            db.setTransactionSuccessful()
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        } finally {
            db.endTransaction()
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

    fun getExistingSyncIdsForCreateNewOrUpdate(tableName: String, objectIds: List<UUID>): Set<UUID> {
        val placeholders = objectIds.joinToString(",") { "?" }
        val args = objectIds.map { it.toString() }.toTypedArray()
        val query = """
            SELECT ObjectID FROM SynchronizeData
            WHERE TableName = ? AND ObjectID IN ($placeholders)
        """
        val cursor = db.rawQuery(query, arrayOf(tableName, *args))

        val existingIds = mutableSetOf<UUID>()
        while (cursor.moveToNext()) {
            val idStr = cursor.getString(cursor.getColumnIndexOrThrow("ObjectID"))
            existingIds.add(UUID.fromString(idStr))
        }
        cursor.close()
        return existingIds
    }

}