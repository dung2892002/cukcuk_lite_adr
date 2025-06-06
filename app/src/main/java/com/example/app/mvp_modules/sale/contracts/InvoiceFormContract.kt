package com.example.app.mvp_modules.sale.contracts

import com.example.app.dto.InventorySelect
import com.example.app.entities.Invoice
import com.example.app.dto.SeverResponse

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
        suspend fun handleDataInventorySelect(invoice: Invoice) : MutableList<InventorySelect>
        fun calculatorTotalPrice(dishesSelect: MutableList<InventorySelect>)
        suspend fun paymentInvoice(inventoriesSelect: MutableList<InventorySelect>, invoice: Invoice)
        suspend fun submitInvoice(invoice: Invoice, inventoriesSelect: MutableList<InventorySelect>, toPayment: Boolean)
    }

}