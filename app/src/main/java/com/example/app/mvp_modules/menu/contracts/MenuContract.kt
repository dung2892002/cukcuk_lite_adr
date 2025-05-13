package com.example.app.mvp_modules.menu.contracts

import com.example.app.models.Dish

interface MenuContract {
    interface View {
        fun navigateToDishForm(dish: Dish?)
        fun showDataDishes(data: MutableList<Dish>)
    }

    interface Presenter {
        fun openDishForm(dish: Dish?)
        fun fetchData()
    }
}