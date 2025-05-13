package com.example.app.mvp_modules.sale.presenters

import com.example.app.models.Bill
import com.example.app.models.SeverResponse
import com.example.app.mvp_modules.sale.contracts.BillContract
import kotlin.random.Random

class BillPresenter(private val view: BillContract.View) : BillContract.Presenter {
    override fun handleCreateBill(bill: Bill): SeverResponse {
        println(bill)
        var response = SeverResponse(true, "Thu tiền thành công")
        val intRandom = Random.Default.nextInt(100)
        if (intRandom % 2 == 0) {
            response.isSuccess = false
            response.message = "Có lỗi xảy ra"
        }
        return response
    }
}