package com.example.app.mvp_modules.menu.presenters

import com.example.app.models.Dish
import com.example.app.models.UnitDish
import com.example.app.mvp_modules.menu.contracts.MenuContract
import java.util.UUID

class MenuPresenter(private val view: MenuContract.View) : MenuContract.Presenter {
    override fun openDishForm(dish: Dish?) {
        view.navigateToDishForm(dish)
    }


    //call api
    override fun fetchData() {
        println("fetching data.....")
        var dishes = testData()
        view.showDataDishes(dishes)
    }

    private fun testData() : MutableList<Dish> {
        var dishes = mutableListOf<Dish>()
        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )

        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "100 cuốn chả giỏ",
                price = 14000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )

        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "10 chai coca 2lit",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )

        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "10 chai nước mắm",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )

        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "50 cái sandwich",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )

        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )

        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )
        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )

        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )

        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )

        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )

        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )
        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )
        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )
        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )
        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )
        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )
        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )
        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )

        dishes.add(
            Dish(
                id = UUID.randomUUID(),
                name = "Bò húc",
                price = 10000.0,
                unit = UnitDish("Chai"),
                color = "#FF00FF00",
                image = "_1_banh_cuon.png",
                isActive = true
            )
        )

        return dishes
    }
}