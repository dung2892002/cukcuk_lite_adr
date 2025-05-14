package com.example.app.mvp_modules.sale.contracts

import com.example.app.models.Bill
import com.example.app.models.DishSelect
import com.example.app.models.Order
import com.example.app.models.SeverResponse

interface SelectDishContract {
    interface View{
        fun openCalculatorTable()
        fun openCalculatorPeople()
        fun openCalculatorDish()

        fun updateTotalPrice(newPrice: Double)
        fun navigateToBillActivity(bill: Bill)
        fun closeActivity(response: SeverResponse)
    }

    interface Presenter{
        fun handleDataDishSelect(order: Order) : MutableList<DishSelect>
        fun calculatorTotalPrice(dishesSelect: MutableList<DishSelect>)
        fun createBill(dishesSelect: MutableList<DishSelect>, order: Order)
        fun submitOrder(order: Order)
    }
}