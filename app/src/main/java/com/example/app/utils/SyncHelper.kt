package com.example.app.utils

import com.example.app.datas.CukcukDbHelper
import com.example.app.datas.repositories.SyncRepository
import com.example.app.entities.InvoiceDetail
import java.util.UUID

object SyncHelper {
    private lateinit var syncRepository: SyncRepository

    fun init(dbHelper: CukcukDbHelper) {
        syncRepository = SyncRepository(dbHelper)
    }

    fun insertSync(tableName: String, objectId: UUID) {
        syncRepository.create(tableName, objectId, 0)
    }

    fun updateSync(tableName: String, objectId: UUID) {
        val existingSyncId = syncRepository.getExistingSyncIdForCreateNewOrUpdate(tableName, objectId)
        if (existingSyncId == null) syncRepository.create(tableName, objectId, 1)
    }

    fun deleteSync(tableName: String, objectId: UUID) {

        val existingSyncId = syncRepository.getExistingSyncIdForCreateNew(tableName, objectId)

        if (existingSyncId != null) syncRepository.delete(existingSyncId)
        else {
            syncRepository.deleteDataBeforeCreateDeleteSync(tableName, objectId)
            syncRepository.create(tableName, objectId, 2)
        }
    }

    fun deleteSyncRange(tableName: String, objectIds: List<UUID>) {
        val toDelete = mutableListOf<UUID>()
        val toCreate = mutableListOf<UUID>()

        for (objectId in objectIds) {
            val existingSyncId = syncRepository.getExistingSyncIdForCreateNew(tableName, objectId)

            if (existingSyncId != null) {
                toDelete.add(existingSyncId)
            } else {
                syncRepository.deleteDataBeforeCreateDeleteSync(tableName, objectId)
                toCreate.add(objectId)
            }
        }

        if (toDelete.isNotEmpty()) {
            syncRepository.deleteRange(toDelete)
        }
        if (toCreate.isNotEmpty()) {
            syncRepository.createRange(tableName, toCreate, 2)
        }
    }

    fun createInvoiceDetail(details: MutableList<InvoiceDetail>){
        val ids = details.mapNotNull { it.InvoiceDetailID }
        syncRepository.createRange("InvoiceDetail", ids, 0)
    }

    fun deleteInvoiceDetail(details: List<InvoiceDetail>) {
        val ids = details.mapNotNull { it.InvoiceDetailID }
        deleteSyncRange("InvoiceDetail", ids)
    }

    fun updateInvoiceDetail(details: List<InvoiceDetail>) {
        val ids = details.mapNotNull { it.InvoiceDetailID }
        val existingIds = syncRepository.getExistingSyncIdsForCreateNewOrUpdate("InvoiceDetail", ids)

        val toInsert = ids.filterNot { existingIds.contains(it) }

        if (toInsert.isNotEmpty()) {
            syncRepository.createRange("InvoiceDetail", toInsert, 1)
        }
    }
}