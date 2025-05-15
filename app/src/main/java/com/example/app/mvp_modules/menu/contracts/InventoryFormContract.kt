package com.example.app.mvp_modules.menu.contracts

import com.example.app.entities.Inventory
import com.example.app.entities.SeverResponse

interface InventoryFormContract {
    interface View{
        fun handleSubmitForm()
        fun deleteDish()
        fun openCalculator()
        fun openSelectUnitDish()
    }

    interface Presenter{
        fun handleSubmitForm(inventory:Inventory, isAddNew: Boolean) : SeverResponse
        fun handleDeleteDish(inventory: Inventory) : SeverResponse
    }
}