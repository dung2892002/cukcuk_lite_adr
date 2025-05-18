package com.example.app.mvp_modules.sale.presenters

import android.annotation.SuppressLint
import com.example.app.datas.repositories.InvoiceRepository
import com.example.app.entities.Invoice
import com.example.app.dto.SeverResponse
import com.example.app.mvp_modules.sale.contracts.SaleContract
import kotlin.random.Random

class SalePresenter(private val view: SaleContract.View,
    private val repository: InvoiceRepository) : SaleContract.Presenter {
    @SuppressLint("NewApi")
    override fun fetchData(): MutableList<Invoice> {
        return repository.getListInvoiceNotPayment()
    }

    override fun paymentInvoice(invoice: Invoice) {
        view.navigateToInvoiceActivity(invoice)
    }


    override fun handleNavigateSelectInventory(invoice: Invoice?) {
        view.navigateToSelectInventoryActivity(invoice)
    }

    override fun handleDeleteInvoice(invoice: Invoice): SeverResponse {
        val response = SeverResponse(false, "Có lỗi xảy ra")
        if (invoice.InvoiceID == null) {
            return response
        }
        response.isSuccess =  repository.deleteInvoice(invoice.InvoiceID.toString())
        return response
    }
}