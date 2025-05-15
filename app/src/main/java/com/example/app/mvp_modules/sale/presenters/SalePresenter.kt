package com.example.app.mvp_modules.sale.presenters

import android.annotation.SuppressLint
import com.example.app.entities.Invoice
import com.example.app.entities.InvoiceDetail
import com.example.app.entities.SeverResponse
import com.example.app.mvp_modules.sale.contracts.SaleContract
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random

class SalePresenter(private val view: SaleContract.View) : SaleContract.Presenter {
    @SuppressLint("NewApi")
    override fun fetchData() {
        var invoices = mutableListOf<Invoice>()
//        view.showDataOrders(orders)
//        return
        invoices.add(
            Invoice(
                InvoiceID = UUID.randomUUID(),
                InvoiceType = 1,
                InvoiceNo = "",
                InvoiceDate = LocalDateTime.now(),
                Amount = 50000.0,
                ReceiveAmount = 0.0,
                ReturnAmount = 0.0,
                RemainAmount = 0.0,
                JournalMemo = "",
                PaymentStatus = 0,
                NumberOfPeople = 0,
                TableName = "",
                ListItemName = "Bún đậu (2)",
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                InvoiceDetails = mutableListOf(
                    InvoiceDetail(
                        InvoiceDetailID = null,
                        InvoiceDetailType = 0,
                        InvoiceID = null,
                        InventoryID = null,
                        InventoryName = "Bún đậu",
                        UnitID = null,
                        UnitName = "",
                        Quantity = 2.0,
                        UnitPrice = 10000.0,
                        Amount = 20000.0,
                        Description = "",
                        SortOrder = 1,
                        CreatedDate = LocalDateTime.now(),
                        CreatedBy = "",
                        ModifiedDate = LocalDateTime.now(),
                        ModifiedBy = "",
                    )
                )
            )
        )

        invoices.add(
            Invoice(
                InvoiceID = UUID.randomUUID(),
                InvoiceType = 1,
                InvoiceNo = "",
                InvoiceDate = LocalDateTime.now(),
                Amount = 50000.0,
                ReceiveAmount = 0.0,
                ReturnAmount = 0.0,
                RemainAmount = 0.0,
                JournalMemo = "",
                PaymentStatus = 0,
                NumberOfPeople = 0,
                TableName = "",
                ListItemName = "",
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                InvoiceDetails = mutableListOf(
                    InvoiceDetail(
                        InvoiceDetailID = null,
                        InvoiceDetailType = 0,
                        InvoiceID = null,
                        InventoryID = null,
                        InventoryName = "Bún đậu",
                        UnitID = null,
                        UnitName = "",
                        Quantity = 2.0,
                        UnitPrice = 10000.0,
                        Amount = 20000.0,
                        Description = "",
                        SortOrder = 1,
                        CreatedDate = LocalDateTime.now(),
                        CreatedBy = "",
                        ModifiedDate = LocalDateTime.now(),
                        ModifiedBy = "",
                    ),
                    InvoiceDetail(
                        InvoiceDetailID = null,
                        InvoiceDetailType = 0,
                        InvoiceID = null,
                        InventoryID = null,
                        InventoryName = "Bún chả",
                        UnitID = null,
                        UnitName = "",
                        Quantity = 2.0,
                        UnitPrice = 10000.0,
                        Amount = 20000.0,
                        Description = "",
                        SortOrder = 1,
                        CreatedDate = LocalDateTime.now(),
                        CreatedBy = "",
                        ModifiedDate = LocalDateTime.now(),
                        ModifiedBy = "",
                    ),
                    InvoiceDetail(
                        InvoiceDetailID = null,
                        InvoiceDetailType = 0,
                        InvoiceID = null,
                        InventoryID = null,
                        InventoryName = "Bún đậu",
                        UnitID = null,
                        UnitName = "",
                        Quantity = 2.0,
                        UnitPrice = 10000.0,
                        Amount = 20000.0,
                        Description = "",
                        SortOrder = 1,
                        CreatedDate = LocalDateTime.now(),
                        CreatedBy = "",
                        ModifiedDate = LocalDateTime.now(),
                        ModifiedBy = "",
                    ),
                    InvoiceDetail(
                        InvoiceDetailID = null,
                        InvoiceDetailType = 0,
                        InvoiceID = null,
                        InventoryID = null,
                        InventoryName = "Bún đậu",
                        UnitID = null,
                        UnitName = "",
                        Quantity = 2.0,
                        UnitPrice = 10000.0,
                        Amount = 20000.0,
                        Description = "",
                        SortOrder = 1,
                        CreatedDate = LocalDateTime.now(),
                        CreatedBy = "",
                        ModifiedDate = LocalDateTime.now(),
                        ModifiedBy = "",
                    ),
                    InvoiceDetail(
                        InvoiceDetailID = null,
                        InvoiceDetailType = 0,
                        InvoiceID = null,
                        InventoryID = null,
                        InventoryName = "Bún đậu",
                        UnitID = null,
                        UnitName = "",
                        Quantity = 2.0,
                        UnitPrice = 10000.0,
                        Amount = 20000.0,
                        Description = "",
                        SortOrder = 1,
                        CreatedDate = LocalDateTime.now(),
                        CreatedBy = "",
                        ModifiedDate = LocalDateTime.now(),
                        ModifiedBy = "",
                    )
                )
            )
        )
        view.showDataOrders(invoices)
    }

    override fun createBill(invoice: Invoice) {
        view.navigateToInvoiceActivity(invoice)
    }

    override fun deleteOrder(invoice: Invoice): SeverResponse {
        println(invoice)
        var response = SeverResponse(true, "Xóa thành công")
        val intRandom = Random.Default.nextInt(100)
        if (intRandom % 2 == 0) {
            response.isSuccess = false
            response.message = "Có lỗi xảy ra"
        }
        return response
    }

    override fun handleNavigateSelectDish(invoice: Invoice?) {
        view.navigateToSelectDishActivity(invoice)
    }
}