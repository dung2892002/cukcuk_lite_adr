package com.example.app.mvp_modules.sale.adapters

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.models.Order
import com.example.app.models.OrderDish
import com.example.app.utils.FormatDisplay

class ListOrderAdapter(
    private val context: Context,
    private val orders: MutableList<Order>,
) : RecyclerView.Adapter<ListOrderAdapter.OrderViewHolder>()
{
    var onClickButtonDelete: ((Order) -> Unit)? = null
    var onClickButtonCreateBill: ((Order) -> Unit)? = null
    var onItemClick:((Order) -> Unit)? = null

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numberTable: TextView = itemView.findViewById<TextView>(R.id.txtItemNumberTable)
        val quantityPeople: TextView = itemView.findViewById<TextView>(R.id.txtItemQuantityPeople)
        val listDishes: TextView = itemView.findViewById<TextView>(R.id.txtListDish)
        val totalPrice: TextView = itemView.findViewById<TextView>(R.id.txtTotalPrice)
        val buttonDelete: LinearLayout = itemView.findViewById<LinearLayout>(R.id.btnItemDeleteOrder)
        val buttonCreateBill: LinearLayout = itemView.findViewById<LinearLayout>(R.id.btnItemCreateBill)
        val imageHasPeople: AppCompatImageView = itemView.findViewById<AppCompatImageView>(R.id.imgHasPeople)

        init {
            itemView.setOnClickListener {
                val selected = orders[adapterPosition]
                onItemClick?.invoke(selected)
            }

            buttonDelete.setOnClickListener {
                val selected = orders[adapterPosition]
                onClickButtonDelete?.invoke(selected)
            }

            buttonCreateBill.setOnClickListener {
                val selected = orders[adapterPosition]
                onClickButtonCreateBill?.invoke(selected)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: OrderViewHolder,
        position: Int
    ) {
        val order = orders[position]

        if (order.tableNumber != null) {
            holder.numberTable.text = order.tableNumber.toString()
        } else {
            holder.numberTable.visibility = View.GONE
        }
        if (order.quantityPeople != null) {
            holder.quantityPeople.text = order.quantityPeople.toString()
        } else {
            holder.quantityPeople.visibility = View.GONE
            holder.imageHasPeople.visibility = View.GONE
        }
        holder.totalPrice.text = FormatDisplay.formatNumber(order.totalPrice.toString())
        holder.listDishes.text = getDishesOrderSpannable(order.dishes)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    private fun getDishesOrderSpannable(ordersDish: MutableList<OrderDish>): SpannableStringBuilder {
        val result = SpannableStringBuilder()

        ordersDish.forEachIndexed { index, orderDish ->
            val name = orderDish.dish?.name ?: ""
            val quantity = "(${orderDish.quantity})"

            result.append("$name ")

            val start = result.length
            result.append(quantity)
            val end = result.length
            result.setSpan(
                ForegroundColorSpan(Color.BLUE),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            if (index != ordersDish.size - 1) {
                result.append(", ")
            }
        }

        return result
    }

}