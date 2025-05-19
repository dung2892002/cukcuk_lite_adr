package com.example.app.mvp_modules.statistic.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.dto.StatisticOverview
import com.example.app.utils.FormatDisplay
import com.example.app.utils.ImageHelper
import com.google.android.material.card.MaterialCardView

class StatisticOverviewAdapter(
    private val context: Context,
    private val items: MutableList<StatisticOverview>,
) : RecyclerView.Adapter<StatisticOverviewAdapter.StatisticOverviewViewHolder>() {

    var onItemClick:((StatisticOverview, Int) -> Unit)? = null

    inner class StatisticOverviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val color: MaterialCardView = itemView.findViewById<MaterialCardView>(R.id.statisticOverviewItemColor)
        val image: ImageView = itemView.findViewById<ImageView>(R.id.statisticOverviewItemIcon)
        val label: TextView = itemView.findViewById<TextView>(R.id.statisticOverviewItemLabel)
        val amount: TextView = itemView.findViewById<TextView>(R.id.statisticOverviewItemAmount)
        val line: View = itemView.findViewById<View>(R.id.statisticOverviewItemLine)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StatisticOverviewViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_statistic_overview, parent, false)
        return StatisticOverviewViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: StatisticOverviewViewHolder,
        position: Int,
    ) {
        val item = items[position]
        holder.color.backgroundTintList = ColorStateList.valueOf(item.Color.toColorInt())
        holder.label.text = item.Title
        holder.amount.text = FormatDisplay.formatNumber(item.Amount.toString())
        if (position == items.size - 1) {
            holder.line.visibility = View.GONE
        }
        val drawable = ImageHelper.getDrawableImageOtherFromAssets(context, item.IconFile)
        holder.image.setImageDrawable(drawable)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}