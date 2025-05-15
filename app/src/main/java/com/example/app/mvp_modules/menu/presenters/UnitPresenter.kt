package com.example.app.mvp_modules.menu.presenters

import android.annotation.SuppressLint
import com.example.app.entities.SeverResponse
import com.example.app.entities.Unit
import com.example.app.mvp_modules.menu.contracts.UnitContract
import java.time.LocalDateTime

class UnitPresenter(private val view: UnitContract.View,
                    private val model: UnitContract.Model) : UnitContract.Presenter {

    override fun getListUnit(): MutableList<Unit> {
        return model.getAllUnit()
    }

    override fun handleUpdateUnitInventory(unit: Unit) {
        view.onChangeUnitInventory()
    }

    override fun handleSubmit(unit: Unit, isAddNew: Boolean) {
        val response = SeverResponse(true, "")
        println("Unit name: ${unit.UnitName}")
        if (isAddNew) {
            response.isSuccess = model.createUnit(unit)
            view.onSubmit(response, true)

        } else {
            response.isSuccess =model.updateUnit(unit)
            view.onSubmit(response, false)
        }
    }

}