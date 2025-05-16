package com.example.app.mvp_modules.sale.models

import com.example.app.datas.repositories.InvoiceRepository
import com.example.app.entities.Invoice
import com.example.app.entities.InvoiceDetail
import com.example.app.mvp_modules.sale.contracts.InvoiceContract
import java.util.UUID

class InvoiceModel(private val repository: InvoiceRepository) : InvoiceContract.Model {
    override fun getListInvoiceDetail(invoiceId: UUID): MutableList<InvoiceDetail> {
        return repository.getListInvoicesDetail(invoiceId)
    }

    override fun paymentInvoice(invoice: Invoice): Boolean {
        return repository.paymentInvoice(invoice)
    }

    override fun getNewInvoiceNo(): String {
        return repository.getNewInvoiceNo()
    }
}