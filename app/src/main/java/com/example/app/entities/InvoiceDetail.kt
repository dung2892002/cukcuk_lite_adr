package com.example.app.entities

import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

data class InvoiceDetail(
    var InvoiceDetailID: UUID?,
    var InvoiceDetailType: Int,
    var InvoiceID: UUID?,
    var InventoryID: UUID?,
    var InventoryName: String,
    var UnitID: UUID?,
    var UnitName: String,
    var Quantity: Double,
    var UnitPrice: Double,
    var Amount: Double,
    var Description: String,
    var SortOrder: Int,
    var CreatedDate: LocalDateTime?,
    var CreatedBy: String,
    var ModifiedDate: LocalDateTime?,
    var ModifiedBy: String
) : Serializable