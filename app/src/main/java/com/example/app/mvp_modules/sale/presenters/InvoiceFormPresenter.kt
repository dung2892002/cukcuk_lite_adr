package com.example.app.mvp_modules.sale.presenters

import android.annotation.SuppressLint
import com.example.app.datas.repositories.InvoiceRepository
import com.example.app.entities.Inventory
import com.example.app.dto.InventorySelect
import com.example.app.entities.Invoice
import com.example.app.entities.InvoiceDetail
import com.example.app.dto.SeverResponse
import com.example.app.mvp_modules.sale.contracts.InvoiceFormContract
import com.example.app.utils.FormatDisplay
import com.example.app.utils.SyncHelper
import java.time.LocalDateTime
import java.util.UUID

@SuppressLint("NewApi")
class InvoiceFormPresenter(private val view: InvoiceFormContract.View,
    private val repository: InvoiceRepository) : InvoiceFormContract.Presenter {

    override fun handleDataInventorySelect(invoice: Invoice): MutableList<InventorySelect> {
        var result = mutableListOf<InventorySelect>()
        val inventories = fetchInventories()

        if (invoice.InvoiceID != null) {
            invoice.InvoiceDetails = getInvoiceDetails(invoice.InvoiceID!!)
        }

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


    override fun submitInvoice(invoice: Invoice, inventoriesSelect: MutableList<InventorySelect>, toPayment: Boolean) {
        var response = SeverResponse(true, "")

        if (invoice.Amount == 0.0) {
            response.isSuccess = false
            response.message = "Vui lòng chọn món"
            view.closeActivity(response)
            return
        }
        val lastInvoicesDetail = if (invoice.InvoiceID != null) repository.getListInvoicesDetail(invoice.InvoiceID!!) else mutableListOf<InvoiceDetail>()
        val filteredList = inventoriesSelect.filter { it.quantity > 0.0 }.toMutableList<InventorySelect>()

        val (news, updates, deletes) = handleProcessInvoiceDetails(invoice, lastInvoicesDetail, filteredList)

        invoice.ListItemName = buildListItemName(news + updates)
        invoice.ReceiveAmount = invoice.Amount

        if (invoice.InvoiceID != null) {
            response.isSuccess = repository.updateInvoice(invoice, news, updates, deletes)
            if (response.isSuccess) {
                SyncHelper.updateSync("Invoice", invoice.InvoiceID!!)
                SyncHelper.deleteInvoiceDetail(deletes)
                SyncHelper.createInvoiceDetail(news)
                SyncHelper.updateInvoiceDetail(updates)
            }
        } else {
            invoice.InvoiceID = UUID.randomUUID()
            response.message = invoice.InvoiceID.toString()
            invoice.InvoiceDetails = news.map {
                it.copy(InvoiceID = invoice.InvoiceID)
            }.toMutableList()
            response.isSuccess = repository.createInvoice(invoice)
            if (response.isSuccess) {
                SyncHelper.insertSync("Invoice", invoice.InvoiceID!!)
                SyncHelper.createInvoiceDetail(invoice.InvoiceDetails)
            }
        }

        if (!response.isSuccess) {
            response.message = "Có lỗi xảy ra!"
        }

        if (!toPayment) view.closeActivity(response)
    }

    @SuppressLint("NewApi")
    override fun paymentInvoice(
        inventoriesSelect: MutableList<InventorySelect>,
        invoice: Invoice
    ) {

        submitInvoice(invoice, inventoriesSelect, true)
        view.navigateToInvoiceActivity(invoice)
    }


    private fun fetchInventories() : MutableList<Inventory> {
        return repository.getAllInventoryInactive()
    }

    private fun getInvoiceDetails(invoiceID: UUID) : MutableList<InvoiceDetail> {
        return repository.getListInvoicesDetail(invoiceID)
    }

    private fun buildListItemName(invoiceDetails: List<InvoiceDetail>) : String {
        var builder = StringBuilder()
        invoiceDetails.forEachIndexed { index, item ->
            val itemName = item.InventoryName
            val quantity = FormatDisplay.formatNumber(item.Quantity.toString())

            builder.append("$itemName ($quantity)")
            if (index != invoiceDetails.lastIndex) {
                builder.append(", ")
            }
        }

        return builder.toString()
    }

    fun handleProcessInvoiceDetails(
        invoice: Invoice,
        details: MutableList<InvoiceDetail>,
        inventorySelects: MutableList<InventorySelect>
    ): Triple<MutableList<InvoiceDetail>, MutableList<InvoiceDetail>, MutableList<InvoiceDetail>> {

        val newDetails = mutableListOf<InvoiceDetail>()
        val updateDetails = mutableListOf<InvoiceDetail>()
        val deleteDetails = mutableListOf<InvoiceDetail>()

        // Tạo map từ InventoryID để dễ tra cứu
        val currentDetailMap = details.associateBy { it.InventoryID }
        val selectedInventoryIds = inventorySelects.map { it.inventory.InventoryID }.toSet()

        // 1. Duyệt các mặt hàng đang được chọn (từ UI)
        for (it in inventorySelects) {
            val matchingDetail = currentDetailMap[it.inventory.InventoryID]
            if (matchingDetail == null) {
                // => Không tồn tại trước đó, là hàng mới
                val newDetail = InvoiceDetail(
                    InvoiceDetailID = UUID.randomUUID(),
                    InvoiceDetailType = 0,
                    InvoiceID = invoice.InvoiceID,
                    InventoryID = it.inventory.InventoryID,
                    InventoryName = it.inventory.InventoryName,
                    UnitID = it.inventory.UnitID,
                    UnitName = it.inventory.UnitName,
                    Quantity = it.quantity,
                    UnitPrice = it.inventory.Price,
                    Amount = it.quantity * it.inventory.Price,
                    Description = "",
                    SortOrder = 0,
                    CreatedDate = LocalDateTime.now(),
                    ModifiedDate = LocalDateTime.now(),
                    CreatedBy = "",
                    ModifiedBy = ""
                )
                newDetails.add(newDetail)
            } else {
                if (matchingDetail.Quantity != it.quantity ) {
                    val updatedDetail = matchingDetail.copy(
                        Quantity = it.quantity,
                        UnitPrice = it.inventory.Price
                    )
                    updateDetails.add(updatedDetail)
                }
            }
        }

        // 2. Tìm các chi tiết bị xóa (không còn trong danh sách chọn)
        for (detail in details) {
            if (detail.InventoryID !in selectedInventoryIds) {
                deleteDetails.add(detail)
            }
        }

        return Triple(newDetails, updateDetails, deleteDetails)
    }

}