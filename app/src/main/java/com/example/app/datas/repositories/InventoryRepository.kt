package com.example.app.datas.repositories

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.example.app.datas.CukcukDbHelper
import com.example.app.entities.Inventory
import com.example.app.utils.getBoolean
import com.example.app.utils.getDouble
import com.example.app.utils.getString
import com.example.app.utils.getUUID
import java.util.UUID


@SuppressLint("Recycle")
class InventoryRepository(dbHelper: CukcukDbHelper) {
    private val db = dbHelper.readableDatabase

    fun getAllInventory() : MutableList<Inventory> {
        val query =""" 
            SELECT 
                i.InventoryID, i.InventoryName, i.Price,
                i.Inactive, i.Color, i.IconFileName,
                u.UnitId, u.UnitName
            FROM 
                Inventory i
                JOIN Unit u ON i.UnitID = u.UnitId
        """.trimIndent()
        val inventoryList = mutableListOf<Inventory>()
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(query,null)
            while (cursor.moveToNext()) {
                val inventory = Inventory(
                    InventoryID = cursor.getUUID("InventoryID"),
                    InventoryCode = "",
                    InventoryName = cursor.getString("InventoryName"),
                    InventoryType = 0,
                    Price = cursor.getDouble("Price"),
                    Description = "",
                    Inactive = cursor.getBoolean("Inactive"),
                    CreatedBy = "",
                    ModifiedBy = "",
                    CreatedDate = null,
                    ModifiedDate = null,
                    Color = cursor.getString("Color"),
                    IconFileName = cursor.getString("IconFileName"),
                    UseCount = 0,
                    UnitID = cursor.getUUID("UnitID"),
                    UnitName = cursor.getString("UnitName")
                )
                inventoryList.add(inventory)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        } finally {
            cursor?.close()
        }
        return inventoryList
    }

    fun getInventoryById(inventoryID: UUID) : Inventory? {
        val query = """
            SELECT 
                i.InventoryID, i.InventoryName, i.Price,
                i.Inactive, i.Color, i.IconFileName,
                u.UnitId, u.UnitName
            FROM 
                Inventory i
                JOIN Unit u ON i.UnitID = u.UnitId
            WHERE i.InventoryID = ?
        """.trimIndent()

        var inventory: Inventory? = null
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(query, arrayOf(inventoryID.toString()))
            if (cursor.moveToFirst()) {
                inventory = Inventory(
                    InventoryID = cursor.getUUID("InventoryID"),
                    InventoryCode = "",
                    InventoryName = cursor.getString("InventoryName"),
                    InventoryType = 0,
                    Price = cursor.getDouble("Price"),
                    Description = "",
                    Inactive = cursor.getBoolean("Inactive"),
                    CreatedBy = "",
                    ModifiedBy = "",
                    CreatedDate = null,
                    ModifiedDate = null,
                    Color = cursor.getString("Color"),
                    IconFileName = cursor.getString("IconFileName"),
                    UseCount = 0,
                    UnitID = cursor.getUUID("UnitID"),
                    UnitName = cursor.getString("UnitName")
                )
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
        finally {
            cursor?.close()
        }
        return inventory
    }


    fun createInventory(inventory: Inventory): Boolean {
        return try {
            val values = ContentValues().apply {
                put("InventoryID", inventory.InventoryID?.toString() ?: UUID.randomUUID().toString())
                put("InventoryCode", inventory.InventoryCode)
                put("InventoryName", inventory.InventoryName)
                put("InventoryType", inventory.InventoryType)
                put("Price", inventory.Price)
                put("Description", inventory.Description)
                put("Inactive", if (inventory.Inactive) 1 else 0)
                put("CreatedBy", inventory.CreatedBy)
                put("ModifiedBy", inventory.ModifiedBy)
                put("CreatedDate", inventory.CreatedDate?.toString())
                put("ModifiedDate", inventory.ModifiedDate?.toString())
                put("Color", inventory.Color)
                put("IconFileName", inventory.IconFileName)
                put("UseCount", inventory.UseCount)
                put("UnitID", inventory.UnitID.toString())
            }

            val result = db.insert("Inventory", null, values)
            result != -1L
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
            false
        }
    }


    fun updateInventory(inventory: Inventory): Boolean {
        if (inventory.InventoryID == null) return false

        return try {
            val values = ContentValues().apply {
                put("InventoryCode", inventory.InventoryCode)
                put("InventoryName", inventory.InventoryName)
                put("InventoryType", inventory.InventoryType)
                put("Price", inventory.Price)
                put("Description", inventory.Description)
                put("Inactive", if (inventory.Inactive) 1 else 0)
                put("ModifiedBy", inventory.ModifiedBy)
                put("ModifiedDate", inventory.ModifiedDate?.toString())
                put("Color", inventory.Color)
                put("IconFileName", inventory.IconFileName)
                put("UseCount", inventory.UseCount)
                put("UnitID", inventory.UnitID.toString())
            }

            val result = db.update(
                "Inventory",
                values,
                "InventoryID = ?",
                arrayOf(inventory.InventoryID.toString())
            )

            result > 0
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
            false
        }
    }


    fun deleteInventory(inventory: Inventory): Boolean {
        if (inventory.InventoryID == null) return false

        return try {
            val result = db.delete(
                "Inventory",
                "InventoryID = ?",
                arrayOf(inventory.InventoryID.toString())
            )
            result > 0
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
            false
        }
    }


    fun checkInventoryIsInInvoice(inventory: Inventory): Boolean {
        if (inventory.InventoryID == null) return false

        val query = """
        SELECT 1 FROM InvoiceDetail i WHERE i.InventoryId = ? LIMIT 1
    """.trimIndent()

        var exists = false
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(query, arrayOf(inventory.InventoryID.toString()))
            exists = cursor.moveToFirst()
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        } finally {
            cursor?.close()
        }

        return exists
    }

}