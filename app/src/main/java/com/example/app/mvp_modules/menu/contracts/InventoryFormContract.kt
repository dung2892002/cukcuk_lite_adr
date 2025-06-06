package com.example.app.mvp_modules.menu.contracts

import com.example.app.entities.Inventory
import com.example.app.dto.SeverResponse
import java.util.UUID

interface InventoryFormContract {
    interface View{
        suspend fun handleSubmitForm()
        suspend fun deleteDish()
        fun openCalculator()
        fun openSelectUnitDish()
    }

    interface Presenter{
        suspend fun handleSubmitForm(inventory:Inventory, isAddNew: Boolean) : SeverResponse
        suspend fun handleDeleteInventory(inventory: Inventory) : SeverResponse
        suspend fun getInventory(inventoryId: UUID) : Inventory?
    }


}