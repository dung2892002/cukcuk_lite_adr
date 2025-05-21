package com.example.app.mvp_modules.menu.contracts

import com.example.app.dto.SeverResponse
import com.example.app.entities.Unit

interface UnitContract {
    interface View{
        fun onClose()
        fun onSubmit(response: SeverResponse, isAddNew: Boolean)
        fun onDelete(response: SeverResponse)
        fun onChangeUnitInventory()
    }

    interface Presenter {
        fun handleUpdateUnitInventory(unit: Unit)
        fun handleSubmit(unit: Unit, isAddNew: Boolean)
        fun handleDelete(unit: Unit)
        fun getListUnit() : MutableList<Unit>
    }
}