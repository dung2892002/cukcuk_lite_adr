package com.example.app.models

import java.io.Serializable
import java.util.UUID

data class OrderDish(
    var id: UUID? = null,
    var orderId: UUID? = null,
    var order: Order?,
    var dishId: UUID,
    var dish: Dish?,
    var quantity: Int,
    var totalPrice: Double
) : Serializable