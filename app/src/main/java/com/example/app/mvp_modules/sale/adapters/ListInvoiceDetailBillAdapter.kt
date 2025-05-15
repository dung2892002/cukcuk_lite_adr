package com.example.app.mvp_modules.sale.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.entities.InvoiceDetail
import com.example.app.utils.FormatDisplay

class ListInvoiceDetailBillAdapter(
    private val context: Context,
    private val items: MutableList<InvoiceDetail>,
) : RecyclerView.Adapter<ListInvoiceDetailBillAdapter.OrderDishViewHolder>()
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_invoice_detail_in_table, parent, false)
        return OrderDishViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: OrderDishViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.dishName.text = item.InventoryName
        holder.orderQuantity.text = FormatDisplay.formatNumber(item.Quantity.toString())
        holder.dishPrice.text = FormatDisplay.formatNumber(item.UnitPrice.toString())
        holder.totalPrice.text = FormatDisplay.formatNumber(item.Amount.toString())
    }

    override fun getItemCount(): Int {
        return items.size
    }
}