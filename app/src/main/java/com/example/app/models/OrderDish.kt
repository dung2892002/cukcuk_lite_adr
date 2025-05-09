package com.example.app.models

import java.io.Serializable
import java.util.UUID

data class OrderDish(
    var id: UUID = UUID.randomUUID(),
    var orderId: UUID,
    var dishId: UUID,
    var quantity: Int,
    var totalPrice: Double
) : Serializable