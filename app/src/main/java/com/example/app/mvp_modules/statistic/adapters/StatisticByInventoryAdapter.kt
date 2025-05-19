package com.example.app.mvp_modules.statistic.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.dto.StatisticByInventory
import com.example.app.utils.FormatDisplay
import com.google.android.material.card.MaterialCardView

class StatisticByInventoryAdapter(
    private val context: Context,
    private var items: MutableList<StatisticByInventory>,
) : RecyclerView.Adapter<StatisticByInventoryAdapter.StatisticByInventoryViewHolder>()
{

    inner class StatisticByInventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val color : MaterialCardView = itemView.findViewById<MaterialCardView>(R.id.statisticByInventoryItemColor)
        val sortOrder: TextView = itemView.findViewById<TextView>(R.id.statisticByInventoryItemSortOrder)
        val inventoryName: TextView = itemView.findViewById<TextView>(R.id.statisticByInventoryItemInventoryName)
        val quantity: TextView = itemView.findViewById<TextView>(R.id.statisticByInventoryItemQuantity)
        val unitName: TextView = itemView.findViewById<TextView>(R.id.statisticByInventoryItemUnitName)
        val amount: TextView = itemView.findViewById<TextView>(R.id.statisticByInventoryItemAmount)
        val line: View = itemView.findViewById<View>(R.id.statisticByInventoryLine)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StatisticByInventoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_statistic_by_inventory, parent, false)
        return StatisticByInventoryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: StatisticByInventoryViewHolder,
        position: Int,
    ) {
        val item = items[position]
        holder.color.backgroundTintList = ColorStateList.valueOf(item.Color.toColorInt())
        holder.sortOrder.text = item.SortOrder.toString()
        holder.inventoryName.text = item.InventoryName
        holder.amount.text = FormatDisplay.formatNumber(item.Amount.toString())
        holder.quantity.text = FormatDisplay.formatNumber(item.Quantity.toString())
        holder.unitName.text = item.UnitName

        if (position == items.size - 1) {
            holder.line.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}