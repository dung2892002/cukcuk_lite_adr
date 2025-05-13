package com.example.app.models
import java.io.Serializable
import java.util.UUID

data class Bill(
    var id: UUID? = null,
    var orderId: UUID? = null,
    var order: Order,
    var createdAt: String,
    var moneyGive: Double,
    var moneyReturn: Double = moneyGive - order.totalPrice
) : Serializable