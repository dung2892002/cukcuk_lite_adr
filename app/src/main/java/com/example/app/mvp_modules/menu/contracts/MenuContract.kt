package com.example.app.mvp_modules.menu.contracts

import com.example.app.entities.Inventory
import java.util.UUID

interface MenuContract {
    interface View {
        fun navigateToInventoryForm(inventoryId: UUID?)
        fun showDataInventories(data: MutableList<Inventory>)
    }

    interface Presenter {
        fun openInventoryForm(inventoryId: UUID?)
        suspend fun fetchData()
    }

}