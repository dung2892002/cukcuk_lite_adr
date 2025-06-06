package com.example.app.mvp_modules.menu.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.entities.Unit
import com.google.android.material.card.MaterialCardView


@SuppressLint("NotifyDataSetChanged")
class ListUnitAdapter(
    context: Context,
    private var units: MutableList<Unit>,
    var selectedUnit: Unit
) : RecyclerView.Adapter<ListUnitAdapter.UnitViewHolder>()
{
    var onUnitSelected: ((Unit) -> kotlin.Unit)? = null
    var onClickEditButton: ((Unit) -> kotlin.Unit)? = null
    var onHoldUnit: ((Unit, View) -> kotlin.Unit)? = null

    fun updateData(newUnits: MutableList<Unit>) {
        this.units = newUnits
        notifyDataSetChanged()
    }

    inner class UnitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val unitName: TextView = view.findViewById(R.id.txtUnitName)
        val imgCheck: MaterialCardView = view.findViewById(R.id.chkUnitDish)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEditUnit)

        init {
            unitName.setOnLongClickListener {
                val selected = units[adapterPosition]
                onHoldUnit?.invoke(selected, it)
                true
            }

            unitName.setOnClickListener {
                val selected = units[adapterPosition]
                onUnitSelected?.invoke(selected)
                this@ListUnitAdapter.selectedUnit = selected
                notifyDataSetChanged()
            }

            view.setOnClickListener {
                val selected = units[adapterPosition]
                onUnitSelected?.invoke(selected)
                this@ListUnitAdapter.selectedUnit = selected
                notifyDataSetChanged()
            }

            btnEdit.setOnClickListener {
                onClickEditButton?.invoke(units[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_unit, parent, false)
        return UnitViewHolder(view)
    }

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) {
        holder.unitName.text = units[position].UnitName
        if (units[position].UnitID == selectedUnit.UnitID) {
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