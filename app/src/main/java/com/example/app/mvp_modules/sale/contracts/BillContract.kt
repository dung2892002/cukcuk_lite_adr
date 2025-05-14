package com.example.app.mvp_modules.sale.contracts

import com.example.app.models.Bill
import com.example.app.models.Order
import com.example.app.models.SeverResponse

interface BillContract {
    interface View{
        fun openCalculator()
        fun navigateCreateOrder(order: Order?)
    }

    interface Presenter{
        fun handleCreateBill(bill: Bill) : SeverResponse
        fun createOrder()
    }
}