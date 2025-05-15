package com.example.app.mvp_modules.menu.models

import com.example.app.datas.repositories.UnitRepository
import com.example.app.entities.Unit
import com.example.app.mvp_modules.menu.contracts.UnitContract

class UnitModel(private val repository: UnitRepository) : UnitContract.Model{
    override fun getAllUnit(): MutableList<Unit> {
        return repository.getAllUnit()
    }

    override fun createUnit(unit: Unit): Boolean {
        return repository.createUnit(unit = unit)
    }

    override fun updateUnit(unit: Unit): Boolean {
        return repository.updateUnit(unit = unit)
    }
}