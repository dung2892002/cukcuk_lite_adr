package com.example.app.mvp_modules.sale.presenters

import com.example.app.dto.SeverResponse
import com.example.app.entities.Invoice
import com.example.app.entities.InvoiceDetail
import com.example.app.mvp_modules.sale.contracts.InvoiceContract

class InvoicePresenter(private val view: InvoiceContract.View, private val model: InvoiceContract.Model) : InvoiceContract.Presenter {
    override fun handlePaymentInvoice(invoice: Invoice): SeverResponse {
        var response = SeverResponse(false, "Có lỗi xảy ra")
        response.isSuccess = model.paymentInvoice(invoice)
        return response
    }

    override fun createInvoice() {
        view.navigateCreateInvoice(null)
    }

    override fun getInvoiceDetails(invoice: Invoice): MutableList<InvoiceDetail> {
        return model.getListInvoiceDetail(invoiceId = invoice.InvoiceID!!)
    }

    override fun getNewInvoiceNo(): String {
        return model.getNewInvoiceNo()
    }
}