package com.example.app.mvp_modules.menu.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.models.UnitDish
import com.google.android.material.card.MaterialCardView

class ListUnitDishAdapter(
    context: Context,
    private val units: MutableList<UnitDish>,
    var selectedUnit: UnitDish
) : RecyclerView.Adapter<ListUnitDishAdapter.UnitViewHolder>()
{
    var onUnitSelected: ((UnitDish) -> Unit)? = null
    var onClickEditButton: ((UnitDish) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    inner class UnitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val unitName: TextView = view.findViewById(R.id.txtUnitName)
        val imgCheck: MaterialCardView = view.findViewById(R.id.chkUnitDish)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEditUnit)

        init {
            view.setOnClickListener {
                val selected = units[adapterPosition]
                onUnitSelected?.invoke(selected)
                this@ListUnitDishAdapter.selectedUnit = selected
                notifyDataSetChanged()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_unit_dish, parent, false)
        return UnitViewHolder(view)
    }

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) {
        holder.unitName.text = units[position].name
        holder.btnEdit.setOnClickListener {
            onClickEditButton?.invoke(units[position])
        }
        if (units[position].name == selectedUnit.name) {
            holder.imgCheck.visibility = View.VISIBLE
        }
        else {
            holder.imgCheck.visibility = View.GONE
        }
    }



    override fun getItemCount(): Int {
        return units.size
    }
}