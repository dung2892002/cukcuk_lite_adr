package com.example.app.mvp_modules.menu.presenters

import com.example.app.entities.Inventory
import com.example.app.entities.SeverResponse
import com.example.app.mvp_modules.menu.contracts.InventoryFormContract
import kotlin.random.Random

class InventoryFormPresenter(private val view: InventoryFormContract.View) : InventoryFormContract.Presenter {
    override fun handleSubmitForm(inventory: Inventory, isAddNew: Boolean) : SeverResponse {
        if (inventory.InventoryName.trim().isEmpty()) {
            var response = SeverResponse(true, "")
            response.isSuccess = false
            response.message = "Tên món ăn không được để trống"
            return response
        }
        return if (isAddNew) {
            createDish(inventory)
        } else {
            updateDish(inventory)
        }
    }

    override fun handleDeleteDish(inventory: Inventory): SeverResponse {
        var response = SeverResponse(true, "Xóa thành công")
        val intRandom = Random.nextInt(100)
        if (intRandom % 2 == 0) {
            response.isSuccess = false
            response.message = "Có lỗi xảy ra"
        }
        return response
    }

    private fun updateDish(inventory: Inventory) : SeverResponse {
        var response = SeverResponse(true, "Sửa món ăn thành công thành công")
        val intRandom = Random.nextInt(100)
        if (intRandom % 2 == 0) {
            response.isSuccess = false
            response.message = "Có lỗi xảy ra"
        }
        return response
    }

    private fun createDish(inventory: Inventory) : SeverResponse {
        var response = SeverResponse(true, "Thêm món ăn thành công")
        val intRandom = Random.nextInt(100)
        if (intRandom % 2 != 0) {
            response.isSuccess = false
            response.message = "Có lỗi xảy ra"
        }
        return response
    }
}