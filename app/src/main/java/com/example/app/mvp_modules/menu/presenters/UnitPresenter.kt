package com.example.app.mvp_modules.menu.presenters

import android.annotation.SuppressLint
import com.example.app.datas.repositories.InventoryRepository
import com.example.app.datas.repositories.UnitRepository
import com.example.app.dto.SeverResponse
import com.example.app.entities.Unit
import com.example.app.mvp_modules.menu.contracts.UnitContract
import java.time.LocalDateTime

class UnitPresenter(private val view: UnitContract.View,
                    private val repository: UnitRepository) : UnitContract.Presenter {

    override fun getListUnit(): MutableList<Unit> {
        return repository.getAllUnit()
    }

    override fun handleUpdateUnitInventory(unit: Unit) {
        view.onChangeUnitInventory()
    }

    override fun handleSubmit(unit: Unit, isAddNew: Boolean) {
        val response = SeverResponse(true, "")
        println("Unit name: ${unit.UnitName}")
        if (isAddNew) {
            response.isSuccess = repository.createUnit(unit)
            view.onSubmit(response, true)

        } else {
            response.isSuccess = repository.updateUnit(unit)
            view.onSubmit(response, false)
        }
    }

}