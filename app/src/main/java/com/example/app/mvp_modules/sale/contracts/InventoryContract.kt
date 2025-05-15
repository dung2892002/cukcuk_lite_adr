package com.example.app.mvp_modules.sale.contracts

import com.example.app.entities.Invoice
import com.example.app.entities.SeverResponse

interface InventoryContract {
    interface View{
        fun openCalculator()
        fun navigateCreateOrder(invoice: Invoice?)
    }

    interface Presenter{
        fun handlePaymentInvoice(invoice: Invoice) : SeverResponse
        fun createOrder()
    }
}