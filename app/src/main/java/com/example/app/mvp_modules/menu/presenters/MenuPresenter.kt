package com.example.app.mvp_modules.menu.presenters

import android.annotation.SuppressLint
import com.example.app.datas.repositories.InventoryRepository
import com.example.app.entities.Inventory
import com.example.app.mvp_modules.menu.contracts.MenuContract
import java.time.LocalDateTime
import java.util.UUID

class MenuPresenter(private val view: MenuContract.View,
                    private val repository: InventoryRepository) : MenuContract.Presenter {
    override fun openInventoryForm(inventoryId: UUID?) {
        view.navigateToInventoryForm(inventoryId)
    }


    override fun fetchData() {
        val dishes = repository.getAllInventory()
        view.showDataInventories(dishes)
    }
}