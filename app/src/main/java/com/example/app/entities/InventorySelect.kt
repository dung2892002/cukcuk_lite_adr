package com.example.app.entities

import java.io.Serializable

data class InventorySelect(
    val inventory: Inventory,
    var quantity: Double
) : Serializable