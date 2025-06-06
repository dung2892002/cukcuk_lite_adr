package com.example.app.mvp_modules.menu.contracts

import com.example.app.dto.SeverResponse
import com.example.app.entities.Unit

interface UnitContract {
    interface View{
        fun onClose()
        suspend fun onSubmit(response: SeverResponse, isAddNew: Boolean)
        fun onDelete(response: SeverResponse)
        fun onChangeUnitInventory()
    }

    interface Presenter {
        fun handleUpdateUnitInventory(unit: Unit)
        suspend fun handleSubmit(unit: Unit, isAddNew: Boolean)
        suspend fun handleDelete(unit: Unit)
        suspend fun getListUnit() : MutableList<Unit>
    }
}