package com.example.app.models

import java.util.UUID

class OrderDish(order: Order, private var dish: Dish, private var quantity: Int) {
    private var id:UUID = UUID.randomUUID()
    private var orderId: UUID = order.getId()
    private var dishId: UUID = dish.getId()
    private var price: Double = dish.getPrice() * quantity

    fun getId() : UUID {
        return this.id
    }

    fun setQuantity(quantity: Int) {
        this.quantity = quantity
        this.price = this.dish.getPrice() * this.quantity
    }
}