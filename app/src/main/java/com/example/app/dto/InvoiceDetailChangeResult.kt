package com.example.app.dto

import com.example.app.entities.InvoiceDetail

data class InvoiceDetailChangeResult(
    val toCreate: MutableList<InvoiceDetail>,
    val toUpdate: MutableList<InvoiceDetail>,
    val toDelete: MutableList<InvoiceDetail>,
    val unchanged: MutableList<InvoiceDetail>
)
