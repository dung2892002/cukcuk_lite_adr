package com.example.app.models

import java.util.UUID

class Dish(
    private var name: String,
    private var price: Double,
    unitDish: UnitDish,
    private var color: String,
    private var image: String
) {
    private var id: UUID = UUID.randomUUID()
    private var unitId: UUID = unitDish.getId()

    fun getId() : UUID {
        return this.id
    }

    fun getPrice() : Double {
        return this.price
    }
}