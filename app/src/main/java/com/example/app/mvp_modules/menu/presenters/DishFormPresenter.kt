package com.example.app.mvp_modules.menu.presenters

import com.example.app.models.Dish
import com.example.app.mvp_modules.menu.contracts.DishFormContract

class DishFormPresenter(private val view: DishFormContract.View) : DishFormContract.Presenter {
    override fun submitForm(dish: Dish, isAddNew: Boolean) {
        val result = if (isAddNew) {
            createDish(dish)
        } else {
            updateDish(dish)
        }
        val message = if (result) "Success" else "Error"
        view.handleSubmitFormResult(result, message)
    }

    private fun updateDish(dish: Dish) : Boolean {
        return dish.name == "hehe"
    }

    private fun createDish(dish: Dish) : Boolean {
        return dish.name == "hehe"
    }
}