package com.example.app.mvp_modules.menu.contracts

import com.example.app.entities.Inventory

interface MenuContract {
    interface View {
        fun navigateToDishForm(inventory: Inventory?)
        fun showDataDishes(data: MutableList<Inventory>)
    }

    interface Presenter {
        fun openDishForm(inventory: Inventory?)
        fun fetchData()
    }

    interface Model {
        fun getAllInventories()
    }
}