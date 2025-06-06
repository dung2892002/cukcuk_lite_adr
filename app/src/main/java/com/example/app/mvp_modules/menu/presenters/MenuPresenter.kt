package com.example.app.mvp_modules.menu.presenters

import com.example.app.datas.repositories.InventoryRepository
import com.example.app.mvp_modules.menu.contracts.MenuContract
import java.util.UUID

class MenuPresenter(private val view: MenuContract.View,
                    private val repository: InventoryRepository) : MenuContract.Presenter {
    override fun openInventoryForm(inventoryId: UUID?) {
        view.navigateToInventoryForm(inventoryId)
    }


    override suspend fun fetchData() {
        val dishes = repository.getAllInventory()
        view.showDataInventories(dishes)
    }
}