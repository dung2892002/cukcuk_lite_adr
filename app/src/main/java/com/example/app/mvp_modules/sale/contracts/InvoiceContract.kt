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
        fun handlePaymentInvoice(invoice: Invoice) : SeverResponse
        fun createInvoice()
        fun getInvoiceDetails(invoice: Invoice) : MutableList<InvoiceDetail>
        fun getNewInvoiceNo(): String
    }

}