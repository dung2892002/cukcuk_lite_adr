package com.example.app.models

import java.util.UUID

class UnitDish(name: String) {
    private val id:UUID = UUID.randomUUID()

    fun getId() : UUID {
        return this.id
    }
}