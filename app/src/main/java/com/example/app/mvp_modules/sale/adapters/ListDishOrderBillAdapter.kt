package com.example.app.mvp_modules.sale.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.models.OrderDish
import com.example.app.utils.FormatDisplay

class ListDishOrderBillAdapter(
    private val context: Context,
    private val orders: MutableList<OrderDish>,
) : RecyclerView.Adapter<ListDishOrderBillAdapter.OrderDishViewHolder>()
{
    inner class OrderDishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dishName: TextView = itemView.findViewById<TextView>(R.id.txtItemDishName)
        val orderQuantity: TextView = itemView.findViewById<TextView>(R.id.txtItemQuantity)
        val dishPrice: TextView = itemView.findViewById<TextView>(R.id.txtItemDishPrice)
        val totalPrice: TextView = itemView.findViewById<TextView>(R.id.txtItemTotalPrice)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderDishViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_dish_in_bill, parent, false)
        return OrderDishViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: OrderDishViewHolder,
        position: Int
    ) {
        val order = orders[position]
        holder.dishName.text = order.dish?.name
        holder.orderQuantity.text = order.quantity.toString()
        holder.dishPrice.text = FormatDisplay.formatNumber(order.dish?.price.toString())
        holder.totalPrice.text = FormatDisplay.formatNumber(order.totalPrice.toString())
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}