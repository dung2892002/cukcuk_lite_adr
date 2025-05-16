package com.example.app.mvp_modules.sale.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.entities.Invoice
import com.example.app.utils.FormatDisplay

@SuppressLint("NotifyDataSetChanged")
class ListInvoiceAdapter(
    private val context: Context,
    private var invoices: MutableList<Invoice>,
) : RecyclerView.Adapter<ListInvoiceAdapter.OrderViewHolder>()
{
    var onClickButtonDelete: ((Invoice) -> Unit)? = null
    var onClickButtonCreateBill: ((Invoice) -> Unit)? = null
    var onItemClick:((Invoice) -> Unit)? = null


    fun updateAdapter(newInvoices: MutableList<Invoice>) {
        invoices = newInvoices
        notifyDataSetChanged()
    }

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
                val selected = invoices[adapterPosition]
                onItemClick?.invoke(selected)
            }

            buttonDelete.setOnClickListener {
                val selected = invoices[adapterPosition]
                onClickButtonDelete?.invoke(selected)
            }

            buttonCreateBill.setOnClickListener {
                val selected = invoices[adapterPosition]
                onClickButtonCreateBill?.invoke(selected)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_invoice, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: OrderViewHolder,
        position: Int
    ) {
        val invoice = invoices[position]

        if (!invoice.TableName.isEmpty()) {
            holder.numberTable.text = invoice.TableName
        } else {
            holder.numberTable.visibility = View.GONE
        }
        if (invoice.NumberOfPeople != 0) {
            holder.quantityPeople.text = invoice.NumberOfPeople.toString()
        } else {
            holder.quantityPeople.visibility = View.GONE
            holder.imageHasPeople.visibility = View.GONE
        }
        holder.totalPrice.text = FormatDisplay.formatNumber(invoice.Amount.toString())
        holder.listDishes.text = spannableFromListItemName(invoice.ListItemName)
    }

    override fun getItemCount(): Int {
        return invoices.size
    }

    private fun spannableFromListItemName(itemListName: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        val items = itemListName.split(", ")

        items.forEachIndexed { index, item ->
            val regex = Regex("(.+?)\\s*\\((\\d+(\\.\\d+)?)\\)")
            val match = regex.find(item)

            if (match != null) {
                val itemName = match.groupValues[1]
                val quantity = match.groupValues[2]

                val startItem = builder.length
                builder.append(itemName)
                builder.setSpan(
                    ForegroundColorSpan(Color.BLACK),
                    startItem,
                    builder.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                builder.setSpan(
                    AbsoluteSizeSpan(14, true),
                    startItem,
                    builder.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                builder.append(" ($quantity)")
                val startQty = builder.length - " ($quantity)".length
                builder.setSpan(
                    ForegroundColorSpan(Color.BLUE),
                    startQty,
                    builder.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                builder.setSpan(
                    AbsoluteSizeSpan(14, true),
                    startQty,
                    builder.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else {
                builder.append(item)
            }

            if (index != items.lastIndex) {
                builder.append(", ")
            }
        }

        return builder
    }


}