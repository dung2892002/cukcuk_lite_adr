package com.example.app.mvp_modules.sale.contracts

import com.example.app.entities.Invoice
import com.example.app.entities.InvoiceDetail
import com.example.app.dto.SeverResponse
import java.util.UUID

interface InvoiceContract {
    interface View{
        fun openCalculator()
        fun navigateCreateInvoice(invoice: Invoice?)
    }

    interface Presenter{
        fun handlePaymentInvoice(invoice: Invoice) : SeverResponse
        fun createInvoice()
        fun getInvoiceDetails(invoice: Invoice) : MutableList<InvoiceDetail>
        fun getNewInvoiceNo(): String
    }

    interface Model {
        fun getListInvoiceDetail(invoiceId: UUID) : MutableList<InvoiceDetail>
        fun paymentInvoice(invoice: Invoice) : Boolean
        fun getNewInvoiceNo(): String
    }
}