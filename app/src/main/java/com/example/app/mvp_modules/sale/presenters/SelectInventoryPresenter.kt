package com.example.app.mvp_modules.sale.presenters

import android.annotation.SuppressLint
import com.example.app.entities.Inventory
import com.example.app.entities.InventorySelect
import com.example.app.entities.Invoice
import com.example.app.entities.InvoiceDetail
import com.example.app.entities.SeverResponse
import com.example.app.mvp_modules.sale.contracts.SelectInventoryContract
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random

class SelectInventoryPresenter(private val view: SelectInventoryContract.View) : SelectInventoryContract.Presenter {
    override fun handleDataDishSelect(invoice: Invoice): MutableList<InventorySelect> {
        var result = mutableListOf<InventorySelect>()
        val inventories = fetchInventories()

        val quantityMap = invoice.InvoiceDetails.associateBy({ it.InventoryID }, { it.Quantity })

        for (inventory in inventories) {
            val quantity = quantityMap[inventory.InventoryID]
            result.add(InventorySelect(inventory, quantity?:0.0))
        }

        return result
    }

    override fun calculatorTotalPrice(inventoriesSelect: MutableList<InventorySelect>) {
        var total = 0.0
        for (inventory in inventoriesSelect) {
            total += inventory.quantity * inventory.inventory.Price
        }

        view.updateTotalPrice(total)
    }

    override fun submitOrder(invoice: Invoice) {
        var response = SeverResponse(true, "")
        if (invoice.Amount == 0.0) {
            response.isSuccess = false
            response.message = "Vui lòng chọn món"
            view.closeActivity(response)
            return
        }
        response = if (invoice.InvoiceID != null) {
            updateOrder(invoice)
        } else {
            createOrder(invoice)
        }
        view.closeActivity(response)
    }

    @SuppressLint("NewApi")
    override fun createBill(
        inventoriesSelect: MutableList<InventorySelect>,
        invoice: Invoice
    ) {
        val orderInventoryMap = invoice.InvoiceDetails.associateBy { it.InventoryID }.toMutableMap()

        for (inventorySelect in inventoriesSelect) {
            val inventoryId = inventorySelect.inventory.InventoryID
            val quantity = inventorySelect.quantity

            val existingInventory = orderInventoryMap[inventoryId]

            if (quantity > 0) {
                if (existingInventory != null) {
                    existingInventory.Quantity = quantity
                } else {
                    val newInvoiceDetail = InvoiceDetail(
                        InvoiceDetailID = null,
                        InvoiceDetailType = 0,
                        InvoiceID = invoice.InvoiceID,
                        InventoryID = inventoryId,
                        InventoryName = inventorySelect.inventory.InventoryName,
                        UnitID = null,
                        UnitName = "",
                        Quantity = inventorySelect.quantity,
                        UnitPrice = 10000.0,
                        Amount = inventorySelect.quantity * inventorySelect.inventory.Price,
                        Description = "",
                        SortOrder = 1,
                        CreatedDate = LocalDateTime.now(),
                        CreatedBy = "",
                        ModifiedDate = LocalDateTime.now(),
                        ModifiedBy = ""
                    )
                    invoice.InvoiceDetails.add(newInvoiceDetail)
                }
            } else {
                if (existingInventory != null) {
                    invoice.InvoiceDetails.remove(existingInventory)
                }
            }
        }

        invoice.ReceiveAmount = invoice.Amount
        view.navigateToInvoiceActivity(invoice)
    }

    private fun updateOrder(invoice: Invoice) : SeverResponse{
        var response = SeverResponse(true, "Cập nhật đặt món thành công")
        val intRandom = Random.Default.nextInt(100)
        if (intRandom % 2 == 0) {
            response.isSuccess = false
            response.message = "Có lỗi xảy ra"
        }
        return response
    }

    private fun createOrder(invoice: Invoice) : SeverResponse{
        var response = SeverResponse(true, "Tạo đặt món thành công")
        val intRandom = Random.Default.nextInt(100)
        if (intRandom % 2 == 0) {
            response.isSuccess = false
            response.message = "Có lỗi xảy ra"
        }
        return response
    }

    //call api lay danh sach mon an
    @SuppressLint("NewApi")
    private fun fetchInventories() : List<Inventory> {
        var inventories = mutableListOf<Inventory>(
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bánh cuốn",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = true,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FFC0CB",
                IconFileName = "_1_banh_cuon.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Khoai chiên",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = true,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#D2B48C",
                IconFileName = "_1_khoai_chien.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Khoai tây chiên",
                InventoryType = 0,
                Price = 15000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FFD2B48C",
                IconFileName = "_1_khoai_tay_chien.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Nem rán",
                InventoryType = 0,
                Price = 15000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FFADFF2F",
                IconFileName = "_1_nem_ran.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Xúc xích",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FF7FFFD4",
                IconFileName = "_1_xuc_xich.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Cam ép",
                InventoryType = 0,
                Price = 20000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FFFFD700",
                IconFileName = "_2_cam_ep.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Dưa hấu ép",
                InventoryType = 0,
                Price = 20000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FFFFC0CB",
                IconFileName = "_2_dua_hau_ep.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bún riêu cua",
                InventoryType = 0,
                Price = 30000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FFFFFF00",
                IconFileName = "_3_bun_rieu_cua.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Cháo lòng",
                InventoryType = 0,
                Price = 20000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FFFF0000",
                IconFileName = "_3_chao_long.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Cháo sườn",
                InventoryType = 0,
                Price = 30000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FF3CB371",
                IconFileName = "_3_chao_suon.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bánh khoai",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FF5F9EA0",
                IconFileName = "_4_banh_khoai.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bánh xèo",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FF0000FF",
                IconFileName = "_4_banh_xeo.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bánh bao",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FFFA8072",
                IconFileName = "_5_banh_bao.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bánh chuối",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FFA52A2A",
                IconFileName = "_5_banh_chuoi.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bánh mì",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FF7FFFD4",
                IconFileName = "_5_banh_mi.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bò húc",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FFF0E68C",
                IconFileName = "_6_redbull.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Cafe",
                InventoryType = 0,
                Price = 30000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#FF808080",
                IconFileName = "_7_cafe.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            ),
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Coca",
                InventoryType = 0,
                Price = 30000.0,
                Description = "",
                Inactive = false,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#DC143C",
                IconFileName = "_7_cocacola.png",
                UseCount = 1,
                UnitID = null,
                UnitName = "test"
            )
        )
        return inventories
    }
}