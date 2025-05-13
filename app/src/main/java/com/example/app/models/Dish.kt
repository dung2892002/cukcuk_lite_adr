package com.example.app.models

import java.io.Serializable
import java.util.UUID


data class Dish(
    var id: UUID? = null,
    var name: String,
    var price: Double,
    var unit: UnitDish,
    var color: String,
    var image: String,
    var isActive: Boolean
) : Serializable
