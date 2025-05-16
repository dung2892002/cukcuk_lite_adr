package com.example.app.mvp_modules.sale.presenters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import com.example.app.entities.Inventory
import com.example.app.dto.InventorySelect
import com.example.app.entities.Invoice
import com.example.app.entities.InvoiceDetail
import com.example.app.dto.SeverResponse
import com.example.app.mvp_modules.sale.contracts.InvoiceFormContract
import com.example.app.utils.FormatDisplay
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random
import kotlin.uuid.Uuid

@SuppressLint("NewApi")
class InvoiceFormPresenter(private val view: InvoiceFormContract.View,
    private val model: InvoiceFormContract.Model) : InvoiceFormContract.Presenter {

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

        invoice.InvoiceDetails = inventoriesSelect.filter {it.quantity > 0}.mapIndexed { index , it->
            InvoiceDetail(
                InvoiceDetailID = null,
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
                SortOrder = index,
                CreatedDate = LocalDateTime.now(),
                ModifiedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedBy = ""
            )
        }.toMutableList()

        invoice.ListItemName = buildListItemName(invoice.InvoiceDetails)
        invoice.ReceiveAmount = invoice.Amount

        val result = if (invoice.InvoiceID != null) {
            model.updateInvoice(invoice)
        } else {
            invoice.InvoiceID = UUID.randomUUID()
            response.message = invoice.InvoiceID.toString()
            model.createInvoice(invoice)
        }

        if (!result) {
            response.isSuccess = false
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
        return model.getInventoriesInactive()
    }

    private fun getInvoiceDetails(invoiceID: UUID) : MutableList<InvoiceDetail> {
        return model.getListInvoiceDetail(invoiceID)
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
}