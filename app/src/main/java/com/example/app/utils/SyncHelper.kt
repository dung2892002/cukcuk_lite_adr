package com.example.app.utils

import com.example.app.datas.CukcukDbHelper
import com.example.app.datas.repositories.SyncRepository
import com.example.app.entities.InvoiceDetail
import java.util.UUID

object SyncHelper {
    lateinit var dbHelper: CukcukDbHelper

    fun insertSync(tableName: String, objectId: UUID) {
        val syncRepo = SyncRepository(dbHelper)
        syncRepo.create(tableName, objectId, action = 0)
    }

    fun updateSync(tableName: String, objectId: UUID) {
        val syncRepo = SyncRepository(dbHelper)
        val existingSyncId = syncRepo.getExistingSyncIdForCreateNewOrUpdate(tableName, objectId)
        if (existingSyncId == null) syncRepo.create(tableName, objectId, 1)
    }

    fun deleteSync(tableName: String, objectId: UUID) {
        val syncRepo = SyncRepository(dbHelper)

        val existingSyncId = syncRepo.getExistingSyncIdForCreateNew(tableName, objectId)

        if (existingSyncId != null) syncRepo.delete(existingSyncId)
        else {
            syncRepo.deleteDataBeforeCreateDeleteSync(tableName, objectId)
            syncRepo.create(tableName, objectId, 2)
        }
    }

    fun deleteInvoiceDetail(details: MutableList<InvoiceDetail>){
        for (item in details) {
            deleteSync("InvoiceDetail", item.InvoiceDetailID!!)
        }
    }

    fun createInvoiceDetail(details: MutableList<InvoiceDetail>){
        for (item in details) {
            insertSync("InvoiceDetail", item.InvoiceDetailID!!)
        }
    }
}