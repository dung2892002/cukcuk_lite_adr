package com.example.app.entities

import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

data class Invoice(
    var InvoiceID: UUID? = null,
    var InvoiceType: Int,
    var InvoiceNo: String,
    var InvoiceDate: LocalDateTime,
    var Amount: Double,
    var ReceiveAmount: Double,
    var ReturnAmount: Double,
    var RemainAmount: Double = 0.0,
    var JournalMemo: String,
    var PaymentStatus: Int,
    var NumberOfPeople: Int,
    var TableName: String,
    var ListItemName: String,
    var CreatedDate: LocalDateTime?,
    var CreatedBy: String,
    var ModifiedDate: LocalDateTime?,
    var ModifiedBy: String,
    var InvoiceDetails: MutableList<InvoiceDetail> = mutableListOf<InvoiceDetail>()
) : Serializable