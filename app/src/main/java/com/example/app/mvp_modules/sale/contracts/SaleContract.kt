package com.example.app.mvp_modules.sale.contracts

import com.example.app.entities.Invoice
import com.example.app.dto.SeverResponse

interface SaleContract {
    interface View{
        fun showDataInvoices(data: MutableList<Invoice>)
        fun navigateToInvoiceActivity(invoice: Invoice)
        fun navigateToSelectInventoryActivity(invoice: Invoice?)
    }

    interface Presenter{
//        fun openDishForm(dish: Dish?)
        fun fetchData() : MutableList<Invoice>
        fun paymentInvoice(invoice: Invoice)
        fun handleNavigateSelectInventory(invoice: Invoice?)
        fun handleDeleteInvoice(invoice: Invoice) : SeverResponse
    }

    interface Model{
        fun getAllInvoiceNotPayment() : MutableList<Invoice>
        fun deleteInvoice(invoiceId: String): Boolean
    }
}