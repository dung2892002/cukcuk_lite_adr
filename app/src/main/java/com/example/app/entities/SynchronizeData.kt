package com.example.app.entities

import java.time.LocalDateTime
import java.util.UUID

data class SynchronizeData (
    var SynchronizeID: UUID,
    var TableName: String,
    var ObjectID: UUID,
    var Action: Int,
    var Data: String,
    var CreatedDate: LocalDateTime,
    var RefID: UUID?,
    var Unit: Unit?,
    var Inventory: Inventory?,
    var Invoice: Invoice?,
    var InvoiceDetail: InvoiceDetail?
)