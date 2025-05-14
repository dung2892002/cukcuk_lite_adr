package com.example.app.mvp_modules.sale.presenters

import com.example.app.models.Bill
import com.example.app.models.Dish
import com.example.app.models.DishSelect
import com.example.app.models.Order
import com.example.app.models.OrderDish
import com.example.app.models.SeverResponse
import com.example.app.models.UnitDish
import com.example.app.mvp_modules.sale.contracts.SelectDishContract
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.random.Random

class SelectDishPresenter(private val view: SelectDishContract.View) : SelectDishContract.Presenter {
    override fun handleDataDishSelect(order: Order): MutableList<DishSelect> {
        var result = mutableListOf<DishSelect>()
        val dishes = fetchDish()

        val quantityMap = order.dishes.associateBy({ it.dishId }, { it.quantity })

        for (dish in dishes) {
            val quantity = quantityMap[dish.id]
            result.add(DishSelect(dish, quantity?: 0))
        }

        return result
    }

    override fun calculatorTotalPrice(dishesSelect: MutableList<DishSelect>) {
        var total = 0.0
        for (dish in dishesSelect) {
            total += dish.quantity * dish.dish.price
        }

        view.updateTotalPrice(total)
    }

    override fun submitOrder(order: Order) {
        var response = SeverResponse(true, "")
        if (order.totalPrice == 0.0) {
            response.isSuccess = false
            response.message = "Vui lòng chọn món"
            view.closeActivity(response)
            return
        }
        response = if (order.id != null) {
            updateOrder(order)
        } else {
            createOrder(order)
        }
        view.closeActivity(response)
    }

    override fun createBill(
        dishesSelect: MutableList<DishSelect>,
        order: Order
    ) {
        val orderDishMap = order.dishes.associateBy { it.dishId }.toMutableMap()

        for (dishSelect in dishesSelect) {
            val dishId = dishSelect.dish.id
            val quantity = dishSelect.quantity

            val existingOrderDish = orderDishMap[dishId]

            if (quantity > 0) {
                if (existingOrderDish != null) {
                    existingOrderDish.quantity = quantity
                } else {
                    val newOrderDish = OrderDish(
                        id = null,
                        orderId = order.id,
                        order = null,
                        dishId = dishId!!,
                        dish = dishSelect.dish,
                        quantity = quantity,
                        totalPrice = dishSelect.dish.price * dishSelect.quantity
                    )
                    order.dishes.add(newOrderDish)
                }
            } else {
                if (existingOrderDish != null) {
                    order.dishes.remove(existingOrderDish)
                }
            }
        }

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

    private fun updateOrder(order: Order) : SeverResponse{
        var response = SeverResponse(true, "Cập nhật đặt món thành công")
        val intRandom = Random.Default.nextInt(100)
        if (intRandom % 2 == 0) {
            response.isSuccess = false
            response.message = "Có lỗi xảy ra"
        }
        return response
    }

    private fun createOrder(order: Order) : SeverResponse{
        var response = SeverResponse(true, "Tạo đặt món thành công")
        val intRandom = Random.Default.nextInt(100)
        if (intRandom % 2 == 0) {
            response.isSuccess = false
            response.message = "Có lỗi xảy ra"
        }
        return response
    }

    //call api lay danh sach mon an
    private fun fetchDish() : List<Dish> {
        var dishes = mutableListOf<Dish>(
            Dish(
                id = UUID.randomUUID(),
                name = "Bánh cuốn",
                price = 10000.0,
                unit = UnitDish("Cái"),
                color = "#FFFFC0CB",
                image = "_1_banh_cuon.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Khoai chiên",
                price = 10000.0,
                unit = UnitDish("Cái"),
                color = "#FF000080",
                image = "_1_khoai_chien.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Khoai tây chiên",
                price = 15000.0,
                unit = UnitDish("Cái"),
                color = "#FFD2B48C",
                image = "_1_khoai_tay_chien.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Nem rán",
                price = 15000.0,
                unit = UnitDish("Cái"),
                color = "#FFADFF2F",
                image = "_1_nem_ran.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Xúc xích",
                price = 10000.0,
                unit = UnitDish("Cái"),
                color = "#FF7FFFD4",
                image = "_1_xuc_xich.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Cam ép",
                price = 20000.0,
                unit = UnitDish("Cốc"),
                color = "#FFFFD700",
                image = "_2_cam_ep.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Dưa hấu ép",
                price = 20000.0,
                unit = UnitDish("Cốc"),
                color = "#FFFFC0CB",
                image = "_2_dua_hau_ep.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Bún riêu cua",
                price = 30000.0,
                unit = UnitDish("Suất"),
                color = "#FFFFFF00",
                image = "_3_bun_rieu_cua.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Cháo lòng",
                price = 20000.0,
                unit = UnitDish("Suất"),
                color = "#FFFF0000",
                image = "_3_chao_long.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Cháo sườn",
                price = 30000.0,
                unit = UnitDish("Suất"),
                color = "#FF3CB371",
                image = "_3_chao_suon.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Bánh khoai",
                price = 10000.0,
                unit = UnitDish("Cái"),
                color = "#FF5F9EA0",
                image = "_4_banh_khoai.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Bánh xèo",
                price = 10000.0,
                unit = UnitDish("Cái"),
                color = "#FF0000FF",
                image = "_4_banh_xeo.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Bánh bao",
                price = 10000.0,
                unit = UnitDish("Cái"),
                color = "#FFFA8072",
                image = "_5_banh_bao.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Bánh chuối",
                price = 10000.0,
                unit = UnitDish("Cái"),
                color = "#FFA52A2A",
                image = "_5_banh_chuoi.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Bánh mì",
                price = 10000.0,
                unit = UnitDish("Cái"),
                color = "#FF7FFFD4",
                image = "_5_banh_mi.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Lon"),
                color = "#FFF0E68C",
                image = "_6_redbull.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Cafe",
                price = 30000.0,
                unit = UnitDish("Cốc"),
                color = "#FF808080",
                image = "_7_cafe.png",
                isActive = true
            ),
            Dish(
                id = UUID.randomUUID(),
                name = "Coca",
                price = 30000.0,
                unit = UnitDish("Lon"),
                color = "#FFDC143C",
                image = "_7_cocacola.png",
                isActive = true
            )
        )
        return dishes
    }
}