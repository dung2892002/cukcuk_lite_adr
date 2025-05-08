package com.example.app.models

import java.util.UUID

class Order(private var tableNumber: Int, private var peopleQuantity: Int) {
    private var id: UUID = UUID.randomUUID()
    private var totalMoney: Double = 0.0

    fun getId() : UUID {
        return this.id
    }

    fun getTotalMoney() : Double {
        return this.totalMoney
    }

    fun setTableNumber(num: Int) {
        this.tableNumber = num
    }

    fun setPeopleQuantity(quantity: Int) {
        this.peopleQuantity = quantity
    }
}