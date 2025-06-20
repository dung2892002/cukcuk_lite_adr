package com.example.app.mvp_modules.menu.presenters

import com.example.app.datas.repositories.UnitRepository
import com.example.app.dto.SeverResponse
import com.example.app.entities.Unit
import com.example.app.mvp_modules.menu.contracts.UnitContract
import com.example.app.utils.SyncHelper
import java.util.UUID

class UnitPresenter(private val view: UnitContract.View,
                    private val repository: UnitRepository) : UnitContract.Presenter {

    override suspend fun getListUnit(): MutableList<Unit> {
        return repository.getAllUnit()
    }

    override fun handleUpdateUnitInventory(unit: Unit) {
        view.onChangeUnitInventory()
    }

    override suspend fun handleSubmit(unit: Unit, isAddNew: Boolean) {
        val response = SeverResponse(true, "")
        response.isSuccess = !repository.checkExistUnitName(unit.UnitName, unit.UnitID)
        if (!response.isSuccess) {
            response.message = "Đơn vị tính <${unit.UnitName}> đã tồn tại"
            view.onSubmit(response, true)
            return
        }
        if (isAddNew) {
            unit.UnitID = UUID.randomUUID()
            response.isSuccess = repository.createUnit(unit)

            if (response.isSuccess) SyncHelper.insertSync("Unit", unit.UnitID!!)

            view.onSubmit(response, true)
        } else {
            response.isSuccess = repository.updateUnit(unit)

            if (response.isSuccess) SyncHelper.updateSync("Unit", unit.UnitID!!)

            view.onSubmit(response, false)
        }
    }

    override suspend fun handleDelete(unit: Unit) {
        val response = SeverResponse(true, "")
        response.isSuccess = !repository.checkUseByInventory(unit.UnitID!!)
        if (!response.isSuccess) {
            response.message = "Đơn vị tính <${unit.UnitName}> đã được sử dụng.\nKhông được phép xóa"
            view.onDelete(response)
            return
        }

        response.isSuccess = repository.deleteUnit(unit.UnitID!!)
        if (response.isSuccess) SyncHelper.deleteSync("Unit", unit.UnitID!!)
        view.onDelete(response)
    }
}