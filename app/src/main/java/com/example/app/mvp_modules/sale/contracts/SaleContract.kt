package com.example.app.mvp_modules.sale.contracts

import com.example.app.entities.Invoice
import com.example.app.entities.SeverResponse

interface SaleContract {
    interface View{
        fun showDataOrders(data: MutableList<Invoice>)
        fun navigateToInvoiceActivity(invoice: Invoice)
        fun navigateToSelectDishActivity(invoice: Invoice?)
    }

    interface Presenter{
//        fun openDishForm(dish: Dish?)
        fun fetchData()
        fun createBill(invoice: Invoice)
        fun deleteOrder(invoice: Invoice) : SeverResponse
        fun handleNavigateSelectDish(invoice: Invoice?)
    }
}