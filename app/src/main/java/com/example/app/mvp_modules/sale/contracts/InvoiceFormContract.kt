package com.example.app.mvp_modules.sale.contracts

import com.example.app.entities.Inventory
import com.example.app.dto.InventorySelect
import com.example.app.entities.Invoice
import com.example.app.entities.InvoiceDetail
import com.example.app.dto.SeverResponse
import java.util.UUID

interface InvoiceFormContract {
    interface View{
        fun openCalculatorTable()
        fun openCalculatorPeople()
        fun openCalculatorDish()

        fun updateTotalPrice(newPrice: Double)
        fun navigateToInvoiceActivity(invoice: Invoice)
        fun closeActivity(response: SeverResponse)
    }

    interface Presenter{
        fun handleDataInventorySelect(invoice: Invoice) : MutableList<InventorySelect>
        fun calculatorTotalPrice(dishesSelect: MutableList<InventorySelect>)
        fun paymentInvoice(inventoriesSelect: MutableList<InventorySelect>, invoice: Invoice)
        fun submitInvoice(invoice: Invoice, inventoriesSelect: MutableList<InventorySelect>, toPayment: Boolean)
    }

    interface Model{
        fun getListInvoiceDetail(invoiceId: UUID) : MutableList<InvoiceDetail>
        fun getInventoriesInactive() : MutableList<Inventory>
        fun createInvoice(invoice: Invoice) : Boolean
        fun updateInvoice(invoice: Invoice) : Boolean
    }
}