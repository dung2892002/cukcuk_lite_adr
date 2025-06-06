package com.example.app.mvp_modules.sale.contracts

import com.example.app.entities.Invoice
import com.example.app.dto.SeverResponse

interface SaleContract {
    interface View{
        fun showDataInvoices(data: MutableList<Invoice>)
        fun navigateToInvoiceActivity(invoice: Invoice)
        fun navigateToInvoiceFormActivity(invoice: Invoice?)
    }

    interface Presenter{
//        fun openDishForm(dish: Dish?)
        suspend fun fetchData() : MutableList<Invoice>
        fun paymentInvoice(invoice: Invoice)
        fun handleNavigateInvoiceForm(invoice: Invoice?)
        suspend fun handleDeleteInvoice(invoice: Invoice) : SeverResponse
    }

}