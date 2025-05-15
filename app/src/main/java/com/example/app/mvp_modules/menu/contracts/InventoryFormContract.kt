package com.example.app.mvp_modules.menu.contracts

import com.example.app.entities.Inventory
import com.example.app.entities.SeverResponse
import java.util.UUID

interface InventoryFormContract {
    interface View{
        fun handleSubmitForm()
        fun deleteDish()
        fun openCalculator()
        fun openSelectUnitDish()
    }

    interface Presenter{
        fun handleSubmitForm(inventory:Inventory, isAddNew: Boolean) : SeverResponse
        fun handleDeleteInventory(inventory: Inventory) : SeverResponse
        fun getInventory(inventoryId: UUID) : Inventory?
    }

    interface Model{
        fun getInventoryDetail(inventoryID: UUID) : Inventory?
        fun createInventory(inventory: Inventory) : Boolean
        fun updateInventory(inventory: Inventory) : Boolean
        fun deleteInventory(inventory: Inventory) : Boolean
        fun checkInventoryInInvoice(inventory: Inventory) : Boolean
    }
}