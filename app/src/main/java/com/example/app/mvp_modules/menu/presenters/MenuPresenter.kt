package com.example.app.mvp_modules.menu.presenters

import android.annotation.SuppressLint
import com.example.app.entities.Inventory
import com.example.app.mvp_modules.menu.contracts.MenuContract
import java.time.LocalDateTime
import java.util.UUID

class MenuPresenter(private val view: MenuContract.View) : MenuContract.Presenter {
    override fun openDishForm(inventory: Inventory?) {
        view.navigateToDishForm(inventory)
    }


    //call api
    override fun fetchData() {
        println("fetching data.....")
        var dishes = testData()
        view.showDataDishes(dishes)
    }

    @SuppressLint("NewApi")
    private fun testData() : MutableList<Inventory> {
        var inventories =  mutableListOf(
            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bò Húc",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = true,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#00FF00",
                IconFileName = "_1_banh_cuon.png",
                UseCount = 0,
                UnitID = null,
                UnitName = "Cái"
            ),

            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bò Húc",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = true,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#00FF00",
                IconFileName = "_1_banh_cuon.png",
                UseCount = 0,
                UnitID = null,
                UnitName = "Cái"
            ),

            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bò Húc",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = true,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#00FF00",
                IconFileName = "_1_banh_cuon.png",
                UseCount = 0,
                UnitID = null,
                UnitName = "Cái"
            ),

            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bò Húc",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = true,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#00FF00",
                IconFileName = "_1_banh_cuon.png",
                UseCount = 0,
                UnitID = null,
                UnitName = "Cái"
            ),

            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bò Húc",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = true,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#00FF00",
                IconFileName = "_1_banh_cuon.png",
                UseCount = 0,
                UnitID = null,
                UnitName = "Cái"
            ),

            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bò Húc",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = true,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#00FF00",
                IconFileName = "_1_banh_cuon.png",
                UseCount = 0,
                UnitID = null,
                UnitName = "Cái"
            ),

            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bò Húc",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = true,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#00FF00",
                IconFileName = "_1_banh_cuon.png",
                UseCount = 0,
                UnitID = null,
                UnitName = "Cái"
            ),

            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bò Húc",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = true,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#00FF00",
                IconFileName = "_1_banh_cuon.png",
                UseCount = 0,
                UnitID = null,
                UnitName = "Cái"
            ),

            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bò Húc",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = true,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#00FF00",
                IconFileName = "_1_banh_cuon.png",
                UseCount = 0,
                UnitID = null,
                UnitName = "Cái"
            ),

            Inventory(
                InventoryID = UUID.randomUUID(),
                InventoryCode = "",
                InventoryName = "Bò Húc",
                InventoryType = 0,
                Price = 10000.0,
                Description = "",
                Inactive = true,
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                Color = "#00FF00",
                IconFileName = "_1_banh_cuon.png",
                UseCount = 0,
                UnitID = null,
                UnitName = "Cái"
            ),
        )

        return inventories
    }
}