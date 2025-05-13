package com.example.app.models

import java.io.Serializable
import java.util.UUID

data class Order(
    var id: UUID? = null,
    var tableNumber: Int? = null,
    var quantityPeople: Int? = null,
    var totalPrice: Double,
    var dishes: MutableList<OrderDish> = mutableListOf<OrderDish>()
) : Serializable