package com.example.app.mvp_modules.sale.contracts

import com.example.app.entities.InventorySelect
import com.example.app.entities.Invoice
import com.example.app.entities.SeverResponse

interface SelectInventoryContract {
    interface View{
        fun openCalculatorTable()
        fun openCalculatorPeople()
        fun openCalculatorDish()

        fun updateTotalPrice(newPrice: Double)
        fun navigateToInvoiceActivity(invoice: Invoice)
        fun closeActivity(response: SeverResponse)
    }

    interface Presenter{
        fun handleDataDishSelect(invoice: Invoice) : MutableList<InventorySelect>
        fun calculatorTotalPrice(dishesSelect: MutableList<InventorySelect>)
        fun createBill(inventoriesSelect: MutableList<InventorySelect>, invoice: Invoice)
        fun submitOrder(invoice: Invoice)
    }
}