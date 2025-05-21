package com.example.app.entities

import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

data class Unit(
    var UnitID: UUID? = null,
    var UnitName: String,
    var Description: String,
    var Inactive: Boolean,
    var CreatedDate: LocalDateTime?,
    var CreatedBy: String,
    var ModifiedDate: LocalDateTime?,
    var ModifiedBy: String
) : Serializable