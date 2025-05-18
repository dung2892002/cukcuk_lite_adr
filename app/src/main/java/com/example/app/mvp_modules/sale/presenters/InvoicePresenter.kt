package com.example.app.mvp_modules.sale.presenters

import com.example.app.datas.repositories.InventoryRepository
import com.example.app.datas.repositories.InvoiceRepository
import com.example.app.dto.SeverResponse
import com.example.app.entities.Invoice
import com.example.app.entities.InvoiceDetail
import com.example.app.mvp_modules.sale.contracts.InvoiceContract

class InvoicePresenter(private val view: InvoiceContract.View, private val repository: InvoiceRepository) : InvoiceContract.Presenter {
    override fun handlePaymentInvoice(invoice: Invoice): SeverResponse {
        var response = SeverResponse(false, "Có lỗi xảy ra")
        response.isSuccess = repository.paymentInvoice(invoice)
        return response
    }

    override fun createInvoice() {
        view.navigateCreateInvoice(null)
    }

    override fun getInvoiceDetails(invoice: Invoice): MutableList<InvoiceDetail> {
        return repository.getListInvoicesDetail(invoiceId = invoice.InvoiceID!!)
    }

    override fun getNewInvoiceNo(): String {
        return repository.getNewInvoiceNo()
    }
}