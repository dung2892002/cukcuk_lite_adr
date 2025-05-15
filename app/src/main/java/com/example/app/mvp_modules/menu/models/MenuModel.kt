package com.example.app.mvp_modules.menu.models

import android.content.Context
import com.example.app.datas.repositories.InventoryRepository
import com.example.app.mvp_modules.menu.contracts.MenuContract

class MenuModel(private val repository: InventoryRepository) : MenuContract.Model {
    override fun getAllInventories() {
        TODO("Not yet implemented")
    }
}