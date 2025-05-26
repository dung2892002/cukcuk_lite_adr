package com.example.app.mvp_modules.sale.presenters

import android.annotation.SuppressLint
import com.example.app.datas.repositories.InvoiceRepository
import com.example.app.entities.Inventory
import com.example.app.dto.InventorySelect
import com.example.app.dto.InvoiceDetailChangeResult
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
        val inventories = fetchInventories()

        if (invoice.InvoiceID != null) {
            invoice.InvoiceDetails = getInvoiceDetails(invoice.InvoiceID!!)
        }

        // Map để lấy quantity và sortOrder
        val detailMap = invoice.InvoiceDetails.associateBy { it.InventoryID }

        // Gắn thêm thông tin SortOrder vào InventorySelect rồi mới sort
        val tempList = inventories.map { inventory ->
            val detail = detailMap[inventory.InventoryID]
            Triple(
                InventorySelect(inventory, detail?.Quantity ?: 0.0),
                detail?.SortOrder ?: Int.MAX_VALUE, // Sắp xếp cuối nếu không có
                detail != null
            )
        }

        // Sắp xếp theo SortOrder tăng dần, ưu tiên các bản ghi có detail
        return tempList
            .sortedWith(compareBy({ it.second }, { !it.third })) // sortOrder trước, sau đó là có detail hay không
            .map { it.first }
            .toMutableList()
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

        val result = handleProcessInvoiceDetails(invoice, lastInvoicesDetail, filteredList)

        invoice.InvoiceDetails = (result.toCreate + result.toUpdate + result.unchanged).sortedBy { it.SortOrder }.toMutableList()
        invoice.ListItemName = buildListItemName(invoice.InvoiceDetails)
        invoice.ReceiveAmount = invoice.Amount

        if (invoice.InvoiceID != null) {
            response.isSuccess = repository.updateInvoice(invoice, result.toCreate, result.toUpdate, result.toDelete)
            if (response.isSuccess) {
                SyncHelper.updateSync("Invoice", invoice.InvoiceID!!)
                SyncHelper.deleteInvoiceDetail(result.toDelete)
                SyncHelper.createInvoiceDetail(result.toCreate)
                SyncHelper.updateInvoiceDetail(result.toUpdate)
            }
        } else {
            invoice.InvoiceID = UUID.randomUUID()
            response.message = invoice.InvoiceID.toString()
            invoice.InvoiceDetails = result.toCreate.map {
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
    ): InvoiceDetailChangeResult {

        val toCreate = mutableListOf<InvoiceDetail>()
        val toUpdate = mutableListOf<InvoiceDetail>()
        val toDelete = mutableListOf<InvoiceDetail>()
        val unchanged = mutableListOf<InvoiceDetail>()

        // Tạo map từ InventoryID để dễ tra cứu
        val currentDetailMap = details.associateBy { it.InventoryID }
        val selectedInventoryIds = inventorySelects.map { it.inventory.InventoryID }.toSet()

        var sortOrder = 0

        // 1. Duyệt các mặt hàng đang được chọn (từ UI)
        for (it in inventorySelects) {
            val matchingDetail = currentDetailMap[it.inventory.InventoryID]
            sortOrder++
            if (matchingDetail == null) {
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
                    SortOrder = sortOrder,
                    CreatedDate = LocalDateTime.now(),
                    ModifiedDate = LocalDateTime.now(),
                    CreatedBy = "",
                    ModifiedBy = ""
                )
                toCreate.add(newDetail)
            } else {
                if (matchingDetail.Quantity != it.quantity ) {
                    val updatedDetail = matchingDetail.copy(
                        Quantity = it.quantity,
                        UnitPrice = it.inventory.Price,
                        SortOrder = sortOrder
                    )
                    toUpdate.add(updatedDetail)
                }

                else {
                    val details = matchingDetail.copy(
                        Quantity = it.quantity,
                        UnitPrice = it.inventory.Price,
                        SortOrder = sortOrder
                    )
                    unchanged.add(details)
                }
            }
        }

        // 2. Tìm các chi tiết bị xóa (không còn trong danh sách chọn)
        for (detail in details) {
            if (detail.InventoryID !in selectedInventoryIds) {
                toDelete.add(detail)
            }
        }

        return InvoiceDetailChangeResult(
            toCreate = toCreate,
            toUpdate = toUpdate,
            toDelete = toDelete,
            unchanged = unchanged
        )
    }

}