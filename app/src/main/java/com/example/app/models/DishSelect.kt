package com.example.app.models

import java.io.Serializable

data class DishSelect(
    val dish: Dish,
    var quantity: Int
) : Serializable