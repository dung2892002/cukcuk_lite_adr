package com.example.app.datas.repositories

import android.annotation.SuppressLint
import android.content.ContentValues
import com.example.app.datas.CukcukDbHelper
import com.example.app.entities.Inventory
import com.example.app.entities.Unit
import com.example.app.utils.getBoolean
import com.example.app.utils.getDateTime
import com.example.app.utils.getDouble
import com.example.app.utils.getInt
import com.example.app.utils.getString
import com.example.app.utils.getUUID
import java.util.UUID


@SuppressLint("Recycle")
class InventoryRepository(private val dbHelper: CukcukDbHelper) {
    private val db = dbHelper.readableDatabase

    fun getAllInventory() : MutableList<Inventory> {
        val query = "SELECT i.*, u.UnitName " +
                "FROM Inventory i " +
                "JOIN Unit u " +
                "ON i.UnitID = u.UnitId"

        val cursor = db.rawQuery(query,null)

        val inventoryList = mutableListOf<Inventory>()

        while (cursor.moveToNext()) {
            val inventory = Inventory(
                InventoryID = cursor.getUUID("InventoryID"),
                InventoryCode = cursor.getString("InventoryCode"),
                InventoryName = cursor.getString("InventoryName"),
                InventoryType = cursor.getInt("InventoryType"),
                Price = cursor.getDouble("Price"),
                Description = cursor.getString("Description"),
                Inactive = cursor.getBoolean("Inactive"),
                CreatedBy = cursor.getString("CreatedBy"),
                ModifiedBy = cursor.getString("ModifiedBy"),
                CreatedDate = cursor.getDateTime("CreatedDate"),
                ModifiedDate = cursor.getDateTime("ModifiedDate"),
                Color = cursor.getString("Color"),
                IconFileName = cursor.getString("IconFileName"),
                UseCount = cursor.getInt("UseCount"),
                UnitID = cursor.getUUID("UnitID"),
                UnitName = cursor.getString("UnitName")
            )

            inventoryList.add(inventory)
        }
        cursor.close()
        return inventoryList
    }

    fun getInventoryById(inventoryID: UUID) : Inventory? {
        val query = """
                SELECT i.*, u.UnitName
                FROM Inventory i
                JOIN Unit u ON i.UnitID = u.UnitID
                WHERE i.InventoryID = ?
            """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(inventoryID.toString()))

        var inventory: Inventory? = null

        if (cursor.moveToFirst()) {
            inventory = Inventory(
                InventoryID = cursor.getUUID("InventoryID"),
                InventoryCode = cursor.getString("InventoryCode"),
                InventoryName = cursor.getString("InventoryName"),
                InventoryType = cursor.getInt("InventoryType"),
                Price = cursor.getDouble("Price"),
                Description = cursor.getString("Description"),
                Inactive = cursor.getBoolean("Inactive"),
                CreatedBy = cursor.getString("CreatedBy"),
                ModifiedBy = cursor.getString("ModifiedBy"),
                CreatedDate = cursor.getDateTime("CreatedDate"),
                ModifiedDate = cursor.getDateTime("ModifiedDate"),
                Color = cursor.getString("Color"),
                IconFileName = cursor.getString("IconFileName"),
                UseCount = cursor.getInt("UseCount"),
                UnitID = cursor.getUUID("UnitID"),
                UnitName = cursor.getString("UnitName") // lấy từ bảng Unit
            )
        }

        cursor.close()

        return inventory
    }


    fun createInventory(inventory: Inventory): Boolean {
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
            put("CreatedDate", inventory.CreatedDate.toString())
            put("ModifiedDate", inventory.ModifiedDate.toString())
            put("Color", inventory.Color)
            put("IconFileName", inventory.IconFileName)
            put("UseCount", inventory.UseCount)
            put("UnitID", inventory.UnitID.toString())
        }

        val result = db.insert("Inventory", null, values)
        return result != -1L
    }

    fun updateInventory(inventory: Inventory): Boolean {
        if (inventory.InventoryID == null) return false

        val values = ContentValues().apply {
            put("InventoryCode", inventory.InventoryCode)
            put("InventoryName", inventory.InventoryName)
            put("InventoryType", inventory.InventoryType)
            put("Price", inventory.Price)
            put("Description", inventory.Description)
            put("Inactive", if (inventory.Inactive) 1 else 0)
            put("ModifiedBy", inventory.ModifiedBy)
            put("ModifiedDate", inventory.ModifiedDate.toString())
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
        return result > 0
    }

    fun deleteInventory(inventory: Inventory) : Boolean {
        if (inventory.InventoryID == null) return false
        val result = db.delete(
            "Inventory",
            "InventoryID = ?",
            arrayOf(inventory.InventoryID.toString())
        )

        return result > 0
    }

    fun checkInventoryIsInInvoice(inventory: Inventory) : Boolean {
        if (inventory.InventoryID == null) return false
        val query = """
            SELECT 1 FROM InvoiceDetail i WHERE i.InventoryId = ? LIMIT 1
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(inventory.InventoryID.toString()))
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }
}