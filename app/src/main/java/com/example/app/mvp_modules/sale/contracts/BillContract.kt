package com.example.app.mvp_modules.sale.contracts

import com.example.app.models.Bill
import com.example.app.models.SeverResponse

interface BillContract {
    interface View{
        fun openCalculator()
    }

    interface Presenter{
        fun handleCreateBill(bill: Bill) : SeverResponse
    }
}