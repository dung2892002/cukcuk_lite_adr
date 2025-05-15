package com.example.app.mvp_modules.sale.presenters

import com.example.app.entities.SeverResponse
import com.example.app.entities.Invoice
import com.example.app.mvp_modules.sale.contracts.InventoryContract
import kotlin.random.Random

class InvoicePresenter(private val view: InventoryContract.View) : InventoryContract.Presenter {
    override fun handlePaymentInvoice(invoice: Invoice): SeverResponse {
        println(invoice)
        var response = SeverResponse(true, "Thu tiền thành công")
        val intRandom = Random.Default.nextInt(100)
        if (intRandom % 2 == 0) {
            response.isSuccess = false
            response.message = "Có lỗi xảy ra"
        }
        return response
    }

    override fun createOrder() {
        view.navigateCreateOrder(null)
    }
}