package com.example.app.mvp_modules.menu.contracts

import com.example.app.entities.SeverResponse
import com.example.app.entities.Unit

interface UnitContract {
    interface View{
        fun onClose()
        fun onSubmit(response: SeverResponse, isAddNew: Boolean)
        fun onChangeUnitInventory()
    }

    interface Presenter {
        fun handleUpdateUnitInventory(unit: Unit)
        fun handleSubmit(unit: Unit, isAddNew: Boolean)
        fun getListUnit() : MutableList<Unit>
    }

    interface Model{
        fun getAllUnit(): MutableList<Unit>
        fun createUnit(unit: Unit) : Boolean
        fun updateUnit(unit: Unit): Boolean
    }
}