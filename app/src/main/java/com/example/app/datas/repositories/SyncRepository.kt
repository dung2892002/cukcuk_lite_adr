package com.example.app.datas.repositories

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getDoubleOrNull
import com.example.app.datas.CukcukDbHelper
import com.example.app.entities.SynchronizeData
import com.example.app.utils.getDateTime
import com.example.app.utils.getDateTimeOrNull
import com.example.app.utils.getInt
import com.example.app.utils.getString
import com.example.app.utils.getUUID
import java.time.LocalDateTime
import java.util.UUID


@SuppressLint("NewApi", "Recycle")
class SyncRepository(dbHelper: CukcukDbHelper) {
    private val db = dbHelper.readableDatabase

    fun countSync(): Int {
        val query = """
            SELECT COUNT(*) FROM SynchronizeData WHERE TableName != "InvoiceDetail"
        """.trimIndent()
        var cursor: Cursor? = null
        var countSync = 0
        try {
            cursor = db.rawQuery(query, null)
            cursor.moveToFirst()
            countSync = cursor.getInt(0)
        }
        catch(ex: Exception) {
            ex.printStackTrace()
        }
        finally {
            cursor?.close()
        }

        return countSync
    }

    fun getLastSyncTime() : LocalDateTime? {
        val query = """
            SELECT MAX(LastSyncTime) as LastTime FROM LastSyncTime
        """.trimIndent()

        var cursor: Cursor? = null
        var lastSyncTime: LocalDateTime? = null

        try {
            cursor = db.rawQuery(query, null)
            lastSyncTime = if (cursor.moveToFirst()) cursor.getDateTimeOrNull("LastTime")
            else null
        }
        catch(ex: Exception) {
            ex.printStackTrace()
        }
        finally {
            cursor?.close()
        }
        return lastSyncTime
    }

    fun updateLastSyncTime(lastSyncTime: LocalDateTime) {
        try {
            val values = ContentValues().apply {
                put("LastSyncTime", lastSyncTime.toString())
            }

            val rowsAffected = db.update(
                "LastSyncTime",
                values,
                "GroupID = ?",
                arrayOf("1")
            )

            if (rowsAffected == 0) {
                values.put("GroupID", 1)
                db.insert("LastSyncTime", null, values)
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
    }


    fun getAllSync() : MutableList<SynchronizeData> {
        val query = """
            SELECT SynchronizeID, TableName, ObjectID, "Action", CreatedDate FROM SynchronizeData
        """.trimIndent()

        val results = mutableListOf<SynchronizeData>()
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(query, null)
            while (cursor.moveToNext()) {
                val sync = SynchronizeData(
                    SynchronizeID = cursor.getUUID("SynchronizeID"),
                    TableName = cursor.getString("TableName"),
                    ObjectID = cursor.getUUID("ObjectID"),
                    Action = cursor.getInt("Action"),
                    Data = "",
                    CreatedDate = cursor.getDateTime("CreatedDate"),
                    RefID = null,
                    Unit = null,
                    Inventory = null,
                    Invoice = null,
                    InvoiceDetail = null
                )
                results.add(sync)
            }
        }
        catch (ex: Exception) {
            println(ex)
            ex.printStackTrace()
        }
        finally {
            cursor?.close()
        }

        return results
    }

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