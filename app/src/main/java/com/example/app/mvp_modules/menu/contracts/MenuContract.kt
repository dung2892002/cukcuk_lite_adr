package com.example.app.mvp_modules.menu.contracts

import com.example.app.models.Dish

interface MenuContract {
    interface View {
        fun navigateToDishForm(dish: Dish?)
    }

    interface Presenter {
        fun openDishForm(dish: Dish?)
    }
}