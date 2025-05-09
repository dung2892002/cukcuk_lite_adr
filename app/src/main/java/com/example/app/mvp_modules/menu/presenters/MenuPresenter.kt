package com.example.app.mvp_modules.menu.presenters

import com.example.app.models.Dish
import com.example.app.mvp_modules.menu.contracts.MenuContract

class MenuPresenter(private val view: MenuContract.View) : MenuContract.Presenter {
    override fun openDishForm(dish: Dish?) {
        view.navigateToDishForm(dish)
    }
}