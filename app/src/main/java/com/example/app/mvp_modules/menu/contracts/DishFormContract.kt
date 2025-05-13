package com.example.app.mvp_modules.menu.contracts

import com.example.app.models.Dish
import com.example.app.models.SeverResponse

interface DishFormContract {
    interface View{
        fun handleSubmitForm()
        fun deleteDish()
        fun openCalculator()
        fun openSelectUnitDish()
    }

    interface Presenter{
        fun handleSubmitForm(dish:Dish, isAddNew: Boolean) : SeverResponse
        fun handleDeleteDish(dish: Dish) : SeverResponse
    }
}