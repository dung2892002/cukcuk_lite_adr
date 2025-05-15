package com.example.app.mvp_modules.menu.models

import com.example.app.datas.repositories.InventoryRepository
import com.example.app.entities.Inventory
import com.example.app.mvp_modules.menu.contracts.InventoryFormContract
import java.util.UUID

class InventoryFormModel(private val repository: InventoryRepository) : InventoryFormContract.Model {
    override fun getInventoryDetail(inventoryID: UUID): Inventory? {
        return repository.getInventoryById(inventoryID)
    }

    override fun createInventory(inventory: Inventory): Boolean {
        return repository.createInventory(inventory)
    }

    override fun updateInventory(inventory: Inventory): Boolean {
        return repository.updateInventory(inventory)
    }

    override fun deleteInventory(inventory: Inventory) : Boolean {
        return repository.deleteInventory(inventory)
    }

    override fun checkInventoryInInvoice(inventory: Inventory): Boolean {
        return repository.checkInventoryIsInInvoice(inventory)
    }
}