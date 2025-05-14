package com.example.app.mvp_modules.sale.presenters

import com.example.app.models.Bill
import com.example.app.models.Dish
import com.example.app.models.Order
import com.example.app.models.OrderDish
import com.example.app.models.SeverResponse
import com.example.app.models.UnitDish
import com.example.app.mvp_modules.sale.contracts.SaleContract
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.random.Random

class SalePresenter(private val view: SaleContract.View) : SaleContract.Presenter {
    override fun fetchData() {
        var orders = mutableListOf<Order>()
//        view.showDataOrders(orders)
//        return
        orders.add(
            Order(
                UUID.randomUUID(),
                null,
                null,
                50000.0,
                dishes = mutableListOf(
                    OrderDish(
                        null,
                        null,
                        null,
                        UUID.randomUUID(),
                        dish = Dish(
                            null,
                            "Bún đậu",
                            20000.0,
                            unit = UnitDish("Suất"),
                            color = "",
                            image = "",
                            isActive = true
                        ),
                        5,
                        100000.0
                    )
                )
            )
        )

        orders.add(
            Order(
                UUID.randomUUID(),
                4,
                6,
                450000.0,
                dishes = mutableListOf<OrderDish>(
                    OrderDish(
                        null,
                        null,
                        null,
                        UUID.randomUUID(),
                        dish = Dish(
                            null,
                            "Bún đậu",
                            20000.0,
                            unit = UnitDish("Suất"),
                            color = "",
                            image = "",
                            isActive = true
                        ),
                        3,
                        60000.0
                    ),
                    OrderDish(
                        null,
                        null,
                        null,
                        UUID.randomUUID(),
                        dish = Dish(
                            null,
                            "Bún đậu đầy đủ",
                            30000.0,
                            unit = UnitDish("Suất"),
                            color = "",
                            image = "",
                            isActive = true
                        ),
                        3,
                        90000.0
                    ),
                    OrderDish(
                        null,
                        null,
                        null,
                        UUID.randomUUID(),
                        dish = Dish(
                            null,
                            "100 cuốn chả giò",
                            1000.0,
                            unit = UnitDish("Suất"),
                            color = "",
                            image = "",
                            isActive = true
                        ),
                        100,
                        100000.0
                    ),
                    OrderDish(
                        null,
                        null,
                        null,
                        UUID.randomUUID(),
                        dish = Dish(
                            null,
                            "Bò húc",
                            10000.0,
                            unit = UnitDish("Suất"),
                            color = "",
                            image = "",
                            isActive = true
                        ),
                        5,
                        50000.0
                    ),
                    OrderDish(
                        null,
                        null,
                        null,
                        UUID.randomUUID(),
                        dish = Dish(
                            null,
                            "Coca 2l",
                            15000.0,
                            unit = UnitDish("Suất"),
                            color = "",
                            image = "",
                            isActive = true
                        ),
                        10,
                        150000.0
                    )
                )
            )
        )
        view.showDataOrders(orders)
    }

    override fun createBill(order: Order) {
        var bill = Bill(
            id = UUID.randomUUID(),
            orderId = order.id,
            order = order,
            createdAt = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss",
                Locale.getDefault()
            ).format(Date()),
            moneyGive = order.totalPrice,
            moneyReturn = 0.0
        )
        view.navigateToBillActivity(bill)
    }

    override fun deleteOrder(order: Order): SeverResponse {
        println(order)
        var response = SeverResponse(true, "Xóa thành công")
        val intRandom = Random.Default.nextInt(100)
        if (intRandom % 2 == 0) {
            response.isSuccess = false
            response.message = "Có lỗi xảy ra"
        }
        return response
    }

    override fun handleNavigateSelectDish(order: Order?) {
        view.navigateToSelectDishActivity(order)
    }
}