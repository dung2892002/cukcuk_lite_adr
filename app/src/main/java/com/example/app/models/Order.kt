package com.example.app.models

import java.io.Serializable
import java.util.UUID

data class Order(
    var id: UUID = UUID.randomUUID(),
    var tableNumber: Int,
    var quantityPeople: Int,
    var totalPrice: Double
) : Serializable