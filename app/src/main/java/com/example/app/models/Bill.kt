package com.example.app.models
import java.util.UUID

class Bill(order: Order, private var moneyGive: Double) {
    private var id: UUID = UUID.randomUUID()
    private var orderId: UUID = order.getId()
    private var totalMoney: Double = order.getTotalMoney()
    private var moneyReturn: Double = moneyGive - order.getTotalMoney()


    fun getId() : UUID {
        return this.id
    }
}