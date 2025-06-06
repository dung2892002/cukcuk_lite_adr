package com.example.app.mvp_modules.sale.contracts

import com.example.app.entities.Invoice
import com.example.app.entities.InvoiceDetail
import com.example.app.dto.SeverResponse

interface InvoiceContract {
    interface View{
        fun openCalculator()
        fun navigateInvoiceForm(invoice: Invoice?)
    }

    interface Presenter{
        suspend fun handlePaymentInvoice(invoice: Invoice) : SeverResponse
        fun createInvoice()
        suspend fun getInvoiceDetails(invoice: Invoice) : MutableList<InvoiceDetail>
        suspend fun getNewInvoiceNo(): String
    }

}