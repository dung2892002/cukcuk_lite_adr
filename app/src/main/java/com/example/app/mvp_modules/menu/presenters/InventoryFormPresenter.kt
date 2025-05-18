package com.example.app.mvp_modules.menu.presenters

import com.example.app.datas.repositories.InventoryRepository
import com.example.app.entities.Inventory
import com.example.app.dto.SeverResponse
import com.example.app.mvp_modules.menu.contracts.InventoryFormContract
import java.util.UUID
import kotlin.random.Random

class InventoryFormPresenter
    (private val view: InventoryFormContract.View,
     private val repository: InventoryRepository) : InventoryFormContract.Presenter {

    override fun handleSubmitForm(inventory: Inventory, isAddNew: Boolean) : SeverResponse {
        var response = SeverResponse(true, "")

        if (inventory.InventoryName.trim().isEmpty()) {
            response.isSuccess = false
            response.message = "Tên món ăn không được để trống"
            return response
        }
        if (isAddNew) {
            response.isSuccess = repository.createInventory(inventory)
        } else {
            response.isSuccess = repository.updateInventory(inventory)
        }

        if (!response.isSuccess) response.message = "Có lỗi xảy ra!"

        return response
    }

    override fun handleDeleteInventory(inventory: Inventory): SeverResponse {
        var response = SeverResponse(true, "Xóa thành công")

        var checked = repository.checkInventoryIsInInvoice(inventory)
        if (checked) {
            response.isSuccess = false
            response.message = "Món ăn đã tồn tại trong 1 hóa đơn!"

            return response
        }

        response.isSuccess = repository.deleteInventory(inventory)
        if (!response.isSuccess) response.message = "Có lỗi xảy ra!"
        return response
    }

    override fun getInventory(inventoryId: UUID): Inventory? {
        return repository.getInventoryById(inventoryId)
    }
}