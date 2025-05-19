package com.example.app.mvp_modules.statistic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.dto.StatisticByTime
import com.example.app.utils.FormatDisplay

class StatisticByTimeAdapter(
    private val context: Context,
    private var items: MutableList<StatisticByTime>,
) : RecyclerView.Adapter<StatisticByTimeAdapter.StatisticByTimeViewHolder>(){

    var onItemClick:((StatisticByTime) -> Unit)? = null

    inner class StatisticByTimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeLabel : TextView = itemView.findViewById<TextView>(R.id.statisticByTimeItemLabel)
        val totalAmount: TextView = itemView.findViewById<TextView>(R.id.statisticByTimeItemAmount)
        val line: View = itemView.findViewById<View>(R.id.statisticByTimeLine)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StatisticByTimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_statistic_by_time, parent, false)
        return StatisticByTimeViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: StatisticByTimeViewHolder,
        position: Int,
    ) {
        holder.timeLabel.text = items[position].Title
        holder.totalAmount.text = FormatDisplay.formatNumber(items[position].Amount.toString())

        if (position == items.size - 1) {
            holder.line.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}