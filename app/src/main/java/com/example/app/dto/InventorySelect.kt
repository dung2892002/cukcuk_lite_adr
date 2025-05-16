package com.example.app.dto

import com.example.app.entities.Inventory
import java.io.Serializable

data class InventorySelect(
    val inventory: Inventory,
    var quantity: Double
) : Serializable