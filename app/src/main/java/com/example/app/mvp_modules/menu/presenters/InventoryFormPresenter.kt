package com.example.app.mvp_modules.menu.presenters

import com.example.app.entities.Inventory
import com.example.app.entities.SeverResponse
import com.example.app.mvp_modules.menu.contracts.InventoryFormContract
import java.util.UUID
import kotlin.random.Random

class InventoryFormPresenter
    (private val view: InventoryFormContract.View,
     private val model: InventoryFormContract.Model) : InventoryFormContract.Presenter {

    override fun handleSubmitForm(inventory: Inventory, isAddNew: Boolean) : SeverResponse {
        var response = SeverResponse(true, "")

        if (inventory.InventoryName.trim().isEmpty()) {
            response.isSuccess = false
            response.message = "Tên món ăn không được để trống"
            return response
        }
        if (isAddNew) {
            response.isSuccess = model.createInventory(inventory)
        } else {
            response.isSuccess = model.updateInventory(inventory)
        }

        if (!response.isSuccess) response.message = "Có lỗi xảy ra!"

        return response
    }

    override fun handleDeleteInventory(inventory: Inventory): SeverResponse {
        var response = SeverResponse(true, "Xóa thành công")

        var checked = model.checkInventoryInInvoice(inventory)
        if (checked) {
            response.isSuccess = false
            response.message = "Món ăn đã tồn tại trong 1 hóa đơn!"

            return response
        }

        response.isSuccess = model.deleteInventory(inventory)
        if (!response.isSuccess) response.message = "Có lỗi xảy ra!"
        return response
    }

    override fun getInventory(inventoryId: UUID): Inventory? {
        return model.getInventoryDetail(inventoryId)
    }
}