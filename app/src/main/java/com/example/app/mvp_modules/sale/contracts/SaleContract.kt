package com.example.app.mvp_modules.sale.contracts

import com.example.app.models.Bill
import com.example.app.models.Order
import com.example.app.models.SeverResponse

interface SaleContract {
    interface View{
        fun showDataOrders(data: MutableList<Order>)
        fun navigateToBillActivity(bill: Bill)
        fun navigateToSelectDishActivity(order: Order?)
    }

    interface Presenter{
//        fun openDishForm(dish: Dish?)
        fun fetchData()
        fun createBill(order: Order)
        fun deleteOrder(order: Order) : SeverResponse
        fun handleNavigateSelectDish(order: Order?)
    }
}