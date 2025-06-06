package com.example.app.mvp_modules.sale.presenters

import android.annotation.SuppressLint
import com.example.app.datas.repositories.InvoiceRepository
import com.example.app.entities.Invoice
import com.example.app.dto.SeverResponse
import com.example.app.mvp_modules.sale.contracts.SaleContract
import com.example.app.utils.SyncHelper

class SalePresenter(private val view: SaleContract.View,
    private val repository: InvoiceRepository) : SaleContract.Presenter {
    @SuppressLint("NewApi")
    override suspend fun fetchData(): MutableList<Invoice> {
        return repository.getListInvoiceNotPayment()
    }

    override fun paymentInvoice(invoice: Invoice) {
        view.navigateToInvoiceActivity(invoice)
    }

    override fun handleNavigateInvoiceForm(invoice: Invoice?) {
        view.navigateToInvoiceFormActivity(invoice)
    }

    override suspend fun handleDeleteInvoice(invoice: Invoice): SeverResponse {
        val response = SeverResponse(false, "Có lỗi xảy ra")
        if (invoice.InvoiceID == null) {
            return response
        }
        val invoicesDetail = repository.getListInvoicesDetail(invoice.InvoiceID!!)
        response.isSuccess =  repository.deleteInvoice(invoice.InvoiceID.toString())
        if (response.isSuccess) {
            SyncHelper.deleteInvoiceDetail(invoicesDetail)
            SyncHelper.deleteSync("Invoice", invoice.InvoiceID!!)
        }
        return response
    }
}