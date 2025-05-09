package com.example.app.models

import java.io.Serializable
import java.util.UUID


data class Dish(
    var id: UUID = UUID.randomUUID(),
    var name: String,
    var price: Double,
    var unit: String,
    var color: String,
    var image: String
) : Serializable
