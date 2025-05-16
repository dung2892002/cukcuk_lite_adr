package com.example.app.mvp_modules.sale.models

import com.example.app.datas.repositories.InvoiceRepository
import com.example.app.entities.Invoice
import com.example.app.mvp_modules.sale.contracts.SaleContract

class SaleModel(private val repository: InvoiceRepository) : SaleContract.Model {
    override fun getAllInvoiceNotPayment(): MutableList<Invoice> {
        return repository.getListInvoiceNotPayment()
    }

    override fun deleteInvoice(invoiceId: String): Boolean {
        return repository.deleteInvoice(invoiceId)
    }
}