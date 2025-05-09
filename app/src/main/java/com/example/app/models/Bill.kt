package com.example.app.models
import java.io.Serializable
import java.util.UUID

data class Bill(
    var id: UUID = UUID.randomUUID(),
    var orderId: UUID,
    var totalPrice: Double,
    var createdAt: String,
    var moneyGive: Double,
    var moneyReturn: Double = moneyGive - totalPrice
) : Serializable