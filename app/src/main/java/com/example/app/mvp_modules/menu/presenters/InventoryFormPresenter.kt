package com.example.app.mvp_modules.menu.presenters

import com.example.app.datas.repositories.InventoryRepository
import com.example.app.entities.Inventory
import com.example.app.dto.SeverResponse
import com.example.app.mvp_modules.menu.contracts.InventoryFormContract
import com.example.app.utils.SyncHelper
import java.util.UUID

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

        if (inventory.UnitID == null) {
            response.isSuccess = false
            response.message = "Đơn vị tính không được để trống"
            return response
        }

        if (isAddNew) {
            inventory.InventoryID = UUID.randomUUID()
            response.isSuccess = repository.createInventory(inventory)
            if (response.isSuccess) SyncHelper.insertSync("Inventory", inventory.InventoryID!!)
        } else {
            response.isSuccess = repository.updateInventory(inventory)
            if (response.isSuccess) SyncHelper.updateSync("Inventory", inventory.InventoryID!!)
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
        else {
            SyncHelper.deleteSync("Inventory", inventory.InventoryID!!)
        }
        return response
    }

    override fun getInventory(inventoryId: UUID): Inventory? {
        return repository.getInventoryById(inventoryId)
    }
}