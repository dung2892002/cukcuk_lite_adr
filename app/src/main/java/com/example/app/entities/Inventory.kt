package com.example.app.entities

import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID


data class Inventory(
    var InventoryID: UUID? = null,
    var InventoryCode: String,
    var InventoryName: String,
    var InventoryType: Int,
    var Price: Double,
    var Description: String,
    var Inactive: Boolean,
    var CreatedDate: LocalDateTime?,
    var CreatedBy: String,
    var ModifiedDate: LocalDateTime?,
    var ModifiedBy: String,
    var Color: String,
    var IconFileName: String,
    var UseCount: Int,
    var UnitID: UUID? = null,
    var UnitName: String
) : Serializable
