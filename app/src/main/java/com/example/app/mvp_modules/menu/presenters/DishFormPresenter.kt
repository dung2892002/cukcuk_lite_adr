package com.example.app.mvp_modules.menu.presenters

import com.example.app.models.Dish
import com.example.app.models.SeverResponse
import com.example.app.mvp_modules.menu.contracts.DishFormContract
import kotlin.random.Random

class DishFormPresenter(private val view: DishFormContract.View) : DishFormContract.Presenter {
    override fun handleSubmitForm(dish: Dish, isAddNew: Boolean) : SeverResponse {
        if (dish.name.trim().isEmpty()) {
            var response = SeverResponse(true, "")
            response.isSuccess = false
            response.message = "Tên món ăn không được để trống"
            return response
        }
        return if (isAddNew) {
            createDish(dish)
        } else {
            updateDish(dish)
        }
    }

    override fun handleDeleteDish(dish: Dish): SeverResponse {
        var response = SeverResponse(true, "Xóa thành công")
        val intRandom = Random.nextInt(100)
        if (intRandom % 2 == 0) {
            response.isSuccess = false
            response.message = "Có lỗi xảy ra"
        }
        return response
    }

    private fun updateDish(dish: Dish) : SeverResponse {
        var response = SeverResponse(true, "Sửa món ăn thành công thành công")
        val intRandom = Random.nextInt(100)
        if (intRandom % 2 == 0) {
            response.isSuccess = false
            response.message = "Có lỗi xảy ra"
        }
        return response
    }

    private fun createDish(dish: Dish) : SeverResponse {
        var response = SeverResponse(true, "Thêm món ăn thành công")
        val intRandom = Random.nextInt(100)
        if (intRandom % 2 != 0) {
            response.isSuccess = false
            response.message = "Có lỗi xảy ra"
        }
        return response
    }
}