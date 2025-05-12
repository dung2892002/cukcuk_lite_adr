package com.example.app.mvp_modules.menu.contracts

import com.example.app.models.UnitDish

interface DishUnitContract {
    interface View{
        fun onClose()
        fun onSubmit()
    }

    interface Presenter {
        fun handleSubmit(unitDish: UnitDish)
    }
}