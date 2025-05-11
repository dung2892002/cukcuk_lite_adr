package com.example.app.mvp_modules.menu.contracts

import com.example.app.models.Dish

interface DishFormContract {
    interface View{
        fun handleSubmitForm()
        fun handleSubmitFormResult(isSuccess: Boolean, message: String)
        fun openCalculator()
        fun openSelectUnitDish()
    }

    interface Presenter{
        fun submitForm(dish:Dish, isAddNew: Boolean)
    }
}