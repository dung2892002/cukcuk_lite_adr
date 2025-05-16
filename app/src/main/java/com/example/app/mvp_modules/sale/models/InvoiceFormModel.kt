package com.example.app.mvp_modules.sale.models

import com.example.app.datas.repositories.InvoiceRepository
import com.example.app.entities.Inventory
import com.example.app.entities.Invoice
import com.example.app.entities.InvoiceDetail
import com.example.app.mvp_modules.sale.contracts.InvoiceFormContract
import java.util.UUID

class InvoiceFormModel(private val repository: InvoiceRepository) : InvoiceFormContract.Model {
    override fun getListInvoiceDetail(invoiceId: UUID): MutableList<InvoiceDetail> {
        return repository.getListInvoicesDetail(invoiceId)
    }

    override fun createInvoice(invoice: Invoice): Boolean {
        return repository.createInvoice(invoice)
    }

    override fun updateInvoice(invoice: Invoice): Boolean {
        return repository.updateInvoice(invoice)
    }

    override fun getInventoriesInactive(): MutableList<Inventory> {
        return repository.getAllInventoryInactive()
    }
}