package com.example.app.datas.repositories

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.example.app.datas.CukcukDbHelper
import com.example.app.entities.Inventory
import com.example.app.entities.Invoice
import com.example.app.entities.InvoiceDetail
import com.example.app.utils.getBoolean
import com.example.app.utils.getDateTime
import com.example.app.utils.getDouble
import com.example.app.utils.getInt
import com.example.app.utils.getString
import com.example.app.utils.getUUID
import java.util.Locale
import java.util.UUID

@SuppressLint("Recycle")
class InvoiceRepository(dbHelper: CukcukDbHelper) {
    private val db = dbHelper.readableDatabase

    fun getNewInvoiceNo() : String {
        var count = 0
        val query = """
            SELECT COUNT(*) FROM Invoice WHERE PaymentStatus = 1
        """.trimIndent()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(query, null)
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
        finally {
            cursor?.close()
        }

        return String.format(Locale.US, "%05d", count + 1)
    }

    fun getListInvoiceNotPayment() : MutableList<Invoice> {
        val invoices = mutableListOf<Invoice>()
        val query = """
            SELECT InvoiceID, InvoiceDate, Amount, NumberOfPeople, TableName, ListItemName, InvoiceDate, ReceiveAmount
            FROM Invoice 
            WHERE PaymentStatus = 0 
            ORDER BY InvoiceDate DESC
        """.trimIndent()

        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(query, null)
            while (cursor.moveToNext()) {
                val invoice = Invoice(
                    InvoiceID = cursor.getUUID("InvoiceID"),
                    InvoiceType = 0,
                    InvoiceNo = "",
                    InvoiceDate = cursor.getDateTime("InvoiceDate"),
                    Amount = cursor.getDouble("Amount"),
                    ReceiveAmount = cursor.getDouble("ReceiveAmount"),
                    ReturnAmount = 0.0,
                    RemainAmount = 0.0,
                    JournalMemo = "",
                    PaymentStatus = 0,
                    NumberOfPeople = cursor.getInt("NumberOfPeople"),
                    TableName = cursor.getString("TableName"),
                    ListItemName = cursor.getString("ListItemName"),
                    CreatedDate = null,
                    CreatedBy = "",
                    ModifiedDate = null,
                    ModifiedBy = "",
                    InvoiceDetails = mutableListOf()
                )
                invoices.add(invoice)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
        finally {
            cursor?.close()
        }
        return invoices
    }

    fun getListInvoicesDetail(invoiceId: UUID) : MutableList<InvoiceDetail> {
        val invoicesDetail = mutableListOf<InvoiceDetail>()
        val query = """
            SELECT 
                InvoiceDetailID, InvoiceID, InventoryID, InventoryName, UnitID, UnitName,
                Quantity, UnitPrice, Amount, SortOrder
            FROM InvoiceDetail 
            WHERE InvoiceID = ? 
            ORDER BY SortOrder
        """.trimIndent()

        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(query, arrayOf(invoiceId.toString()))
            while (cursor.moveToNext()) {
                val invoiceDetail = InvoiceDetail(
                    InvoiceDetailID = cursor.getUUID("InvoiceDetailID"),
                    InvoiceDetailType = 0,
                    InvoiceID = cursor.getUUID("InvoiceID"),
                    InventoryID = cursor.getUUID("InventoryID"),
                    InventoryName = cursor.getString("InventoryName"),
                    UnitID = cursor.getUUID("UnitID"),
                    UnitName = cursor.getString("UnitName"),
                    Quantity = cursor.getDouble("Quantity"),
                    UnitPrice = cursor.getDouble("UnitPrice"),
                    Amount = cursor.getDouble("Amount"),
                    Description = "",
                    SortOrder = cursor.getInt("SortOrder"),
                    CreatedDate = null,
                    CreatedBy = "",
                    ModifiedDate = null,
                    ModifiedBy = ""
                )
                invoicesDetail.add(invoiceDetail)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
        finally {
            cursor?.close()
        }
        return invoicesDetail
    }

    fun deleteInvoice(invoiceId: String) : Boolean {
        return try {
            db.beginTransaction()
            db.delete(
                "InvoiceDetail",
                "InvoiceID = ?",
                arrayOf(invoiceId)
            )
            db.delete(
                "Invoice",
                "InvoiceID = ?",
                arrayOf(invoiceId)
            )

            db.setTransactionSuccessful()
            true
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
            false
        } finally {
            db.endTransaction()
        }
    }

    fun getInvoiceById(invoiceId: UUID): Invoice? {
        var invoice: Invoice? = null
        val invoiceQuery = "SELECT * FROM Invoice WHERE InvoiceID = ?"
        val cursor = db.rawQuery(invoiceQuery, arrayOf(invoiceId.toString()))

        if (cursor.moveToFirst()) {
            invoice = Invoice(
                InvoiceID = cursor.getUUID("InvoiceID"),
                InvoiceType = cursor.getInt("InvoiceType"),
                InvoiceNo = cursor.getString("InvoiceNo"),
                InvoiceDate = cursor.getDateTime("InvoiceDate"),
                Amount = cursor.getDouble("Amount"),
                ReceiveAmount = cursor.getDouble("ReceiveAmount"),
                ReturnAmount = cursor.getDouble("ReturnAmount"),
                RemainAmount = cursor.getDouble("RemainAmount"),
                JournalMemo = cursor.getString("JournalMemo"),
                PaymentStatus = cursor.getInt("PaymentStatus"),
                NumberOfPeople = cursor.getInt("NumberOfPeople"),
                TableName = cursor.getString("TableName"),
                ListItemName = cursor.getString("ListItemName"),
                CreatedDate = cursor.getDateTime("CreatedDate"),
                CreatedBy = cursor.getString("CreatedBy"),
                ModifiedDate = cursor.getDateTime("ModifiedDate"),
                ModifiedBy = cursor.getString("ModifiedBy"),
                InvoiceDetails = mutableListOf()
            )
        }
        cursor.close()

        invoice?.let {
            val detailQuery = "SELECT * FROM InvoiceDetail WHERE InvoiceID = ? ORDER BY SortOrder"
            val detailCursor = db.rawQuery(detailQuery, arrayOf(invoiceId.toString()))
            while (detailCursor.moveToNext()) {
                val detail = InvoiceDetail(
                    InvoiceDetailID = detailCursor.getUUID("InvoiceDetailID"),
                    InvoiceDetailType = detailCursor.getInt("InvoiceDetailType"),
                    InvoiceID = detailCursor.getUUID("InvoiceID"),
                    InventoryID = detailCursor.getUUID("InventoryID"),
                    InventoryName = detailCursor.getString("InventoryName"),
                    UnitID = detailCursor.getUUID("UnitID"),
                    UnitName = detailCursor.getString("UnitName"),
                    Quantity = detailCursor.getDouble("Quantity"),
                    UnitPrice = detailCursor.getDouble("UnitPrice"),
                    Amount = detailCursor.getDouble("Amount"),
                    Description = detailCursor.getString("Description"),
                    SortOrder = detailCursor.getInt("SortOrder"),
                    CreatedDate = detailCursor.getDateTime("CreatedDate"),
                    CreatedBy = detailCursor.getString("CreatedBy"),
                    ModifiedDate = detailCursor.getDateTime("ModifiedDate"),
                    ModifiedBy = detailCursor.getString("ModifiedBy")
                )
                invoice.InvoiceDetails.add(detail)
            }
            detailCursor.close()
        }
        return invoice
    }

    fun getInvoiceDetailById(invoiceDetailId: UUID): InvoiceDetail? {
        var invoiceDetail: InvoiceDetail? = null
        val invoiceQuery = "SELECT * FROM InvoiceDetail WHERE InvoiceDetailID = ?"
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(invoiceQuery, arrayOf(invoiceDetailId.toString()))
            if (cursor.moveToFirst()) {
                invoiceDetail = InvoiceDetail(
                    InvoiceDetailID = cursor.getUUID("InvoiceDetailID"),
                    InvoiceDetailType = cursor.getInt("InvoiceDetailType"),
                    InvoiceID = cursor.getUUID("InvoiceID"),
                    InventoryID = cursor.getUUID("InventoryID"),
                    InventoryName = cursor.getString("InventoryName"),
                    UnitID = cursor.getUUID("UnitID"),
                    UnitName = cursor.getString("UnitName"),
                    Quantity = cursor.getDouble("Quantity"),
                    UnitPrice = cursor.getDouble("UnitPrice"),
                    Amount = cursor.getDouble("Amount"),
                    Description = cursor.getString("Description"),
                    SortOrder = cursor.getInt("SortOrder"),
                    CreatedDate = cursor.getDateTime("CreatedDate"),
                    CreatedBy = cursor.getString("CreatedBy"),
                    ModifiedDate = cursor.getDateTime("ModifiedDate"),
                    ModifiedBy = cursor.getString("ModifiedBy")
                )
            }
        }
        catch (ex: Exception) {
            println(ex)
            ex.printStackTrace()
        }
        finally {
            cursor?.close()
        }




        return invoiceDetail
    }

    fun getAllInventoryInactive() : MutableList<Inventory> {
        val query =""" 
            SELECT 
                i.InventoryID, i.InventoryName, i.Price,
                i.Inactive, i.Color, i.IconFileName,
                u.UnitId, u.UnitName
            FROM 
                Inventory i
                JOIN Unit u ON i.UnitID = u.UnitId
            WHERE i.Inactive = 1
        """

        var cursor: Cursor? = null
        val inventoryList = mutableListOf<Inventory>()

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
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        }
        finally {
            cursor?.close()
        }
        return inventoryList
    }

    fun createInvoice(invoice: Invoice): Boolean {
        return try {
            db.beginTransaction()
            insertInvoice(invoice)
            insertInvoiceDetailRange(invoice.InvoiceDetails)
            db.setTransactionSuccessful()
            true
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
            false
        } finally {
            db.endTransaction()
        }
    }

    fun updateInvoice(invoice: Invoice,
                      newsDetail: MutableList<InvoiceDetail>,
                      updatesDetail: MutableList<InvoiceDetail>,
                      deletesDetail: MutableList<InvoiceDetail>): Boolean {
        return try {
            db.beginTransaction()
            updateInvoiceOnly(invoice)
            insertInvoiceDetailRange(newsDetail)
            updateInvoiceDetailRange(updatesDetail)
            deleteInvoiceDetailRange(deletesDetail.map { it.InvoiceDetailID!! })

            db.setTransactionSuccessful()
            true
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
            false
        } finally {
            db.endTransaction()
        }
    }

    fun paymentInvoice(invoice: Invoice): Boolean {
        return try {
            db.beginTransaction()
            val values = ContentValues().apply {
                put("ReceiveAmount", invoice.ReceiveAmount)
                put("ReturnAmount", invoice.ReturnAmount)
                put("RemainAmount", invoice.RemainAmount)
                put("InvoiceNo", invoice.InvoiceNo)
                put("PaymentStatus", 1)
                put("ModifiedDate", invoice.ModifiedDate.toString())
                put("ModifiedBy", invoice.ModifiedBy)
            }
            db.update("Invoice", values, "InvoiceID = ?", arrayOf(invoice.InvoiceID.toString()))
            db.setTransactionSuccessful()
            true
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
            false
        } finally {
            db.endTransaction()
        }
    }


    private fun insertInvoice(invoice: Invoice) {
        val values = ContentValues().apply {
            put("InvoiceID", invoice.InvoiceID.toString())
            put("InvoiceType", invoice.InvoiceType)
            put("InvoiceNo", invoice.InvoiceNo)
            put("InvoiceDate", invoice.InvoiceDate.toString())
            put("Amount", invoice.Amount)
            put("ReceiveAmount", invoice.ReceiveAmount)
            put("ReturnAmount", invoice.ReturnAmount)
            put("RemainAmount", invoice.RemainAmount)
            put("JournalMemo", invoice.JournalMemo)
            put("PaymentStatus", invoice.PaymentStatus)
            put("NumberOfPeople", invoice.NumberOfPeople)
            put("TableName", invoice.TableName)
            put("ListItemName", invoice.ListItemName)
            put("CreatedDate", invoice.CreatedDate.toString())
            put("CreatedBy", invoice.CreatedBy)
            put("ModifiedDate", invoice.ModifiedDate.toString())
            put("ModifiedBy", invoice.ModifiedBy)
        }
        db.insert("Invoice", null, values)
    }

    private fun insertInvoiceDetailRange(details: List<InvoiceDetail>) {
        for (detail in details) {
            val values = ContentValues().apply {
                put("InvoiceDetailID", detail.InvoiceDetailID.toString())
                put("InvoiceDetailType", detail.InvoiceDetailType)
                put("InvoiceID", detail.InvoiceID.toString())
                put("InventoryID", detail.InventoryID.toString())
                put("InventoryName", detail.InventoryName)
                put("UnitID", detail.UnitID?.toString())
                put("UnitName", detail.UnitName)
                put("Quantity", detail.Quantity)
                put("UnitPrice", detail.UnitPrice)
                put("Amount", detail.Amount)
                put("Description", detail.Description)
                put("SortOrder", detail.SortOrder)
                put("CreatedDate", detail.CreatedDate.toString())
                put("CreatedBy", detail.CreatedBy)
                put("ModifiedDate", detail.ModifiedDate.toString())
                put("ModifiedBy", detail.ModifiedBy)
            }

            db.insert("InvoiceDetail", null, values)
        }
    }

    private fun updateInvoiceOnly(invoice: Invoice) {
        val values = ContentValues().apply {
            put("InvoiceType", invoice.InvoiceType)
            put("InvoiceNo", invoice.InvoiceNo)
            put("InvoiceDate", invoice.InvoiceDate.toString())
            put("Amount", invoice.Amount)
            put("ReceiveAmount", invoice.ReceiveAmount)
            put("ReturnAmount", invoice.ReturnAmount)
            put("RemainAmount", invoice.RemainAmount)
            put("JournalMemo", invoice.JournalMemo)
            put("PaymentStatus", invoice.PaymentStatus)
            put("NumberOfPeople", invoice.NumberOfPeople)
            put("TableName", invoice.TableName)
            put("ListItemName", invoice.ListItemName)
            put("ModifiedDate", invoice.ModifiedDate.toString())
            put("ModifiedBy", invoice.ModifiedBy)
        }

        db.update(
            "Invoice",
            values,
            "InvoiceID = ?",
            arrayOf(invoice.InvoiceID.toString())
        )
    }

    private fun updateInvoiceDetailRange(details: List<InvoiceDetail>) {
        val sql = """
        UPDATE InvoiceDetail SET
            InvoiceDetailType = ?,
            InventoryID = ?,
            InventoryName = ?,
            UnitID = ?,
            UnitName = ?,
            Quantity = ?,
            UnitPrice = ?,
            Amount = ?,
            Description = ?,
            SortOrder = ?,
            ModifiedDate = ?,
            ModifiedBy = ?
        WHERE InvoiceDetailID = ?
    """.trimIndent()

        val statement = db.compileStatement(sql)

        try {
            db.beginTransaction()
            for (detail in details) {
                statement.clearBindings()

                statement.bindLong(1, detail.InvoiceDetailType.toLong())
                statement.bindString(2, detail.InventoryID.toString())
                statement.bindString(3, detail.InventoryName)
                statement.bindString(4, detail.UnitID.toString())
                statement.bindString(5, detail.UnitName )
                statement.bindDouble(6, detail.Quantity)
                statement.bindDouble(7, detail.UnitPrice)
                statement.bindDouble(8, detail.Amount)
                statement.bindString(9, detail.Description)
                statement.bindLong(10, detail.SortOrder.toLong())
                statement.bindString(11, detail.ModifiedDate.toString())
                statement.bindString(12, detail.ModifiedBy )
                statement.bindString(13, detail.InvoiceDetailID.toString())

                statement.executeUpdateDelete()
            }
            db.setTransactionSuccessful()
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        } finally {
            db.endTransaction()
        }
    }

    private fun deleteInvoiceDetailRange(detailIds: List<UUID>) {
        val sql = "DELETE FROM InvoiceDetail WHERE InvoiceDetailID = ?"
        val statement = db.compileStatement(sql)

        try {
            db.beginTransaction()
            for (id in detailIds) {
                statement.clearBindings()
                statement.bindString(1, id.toString())
                statement.executeUpdateDelete()
            }
            db.setTransactionSuccessful()
        } catch (ex: Exception) {
            ex.printStackTrace()
            println(ex)
        } finally {
            db.endTransaction()
        }
    }

}