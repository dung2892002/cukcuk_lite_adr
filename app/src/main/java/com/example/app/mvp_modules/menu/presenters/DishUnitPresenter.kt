package com.example.app.mvp_modules.menu.presenters

import com.example.app.models.UnitDish
import com.example.app.mvp_modules.menu.contracts.DishUnitContract

class DishUnitPresenter(private val view: DishUnitContract.View) : DishUnitContract.Presenter {
    override fun handleSubmit(unitDish: UnitDish) {
        //validate
        view.onSubmit()
    }
}