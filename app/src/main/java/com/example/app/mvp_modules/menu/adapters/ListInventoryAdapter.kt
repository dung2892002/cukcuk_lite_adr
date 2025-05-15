package com.example.app.mvp_modules.menu.adapters

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
import com.example.app.entities.Inventory
import com.example.app.utils.FormatDisplay
import com.example.app.utils.ImageHelper
import com.google.android.material.card.MaterialCardView

class ListInventoryAdapter(
    private val context: Context,
    private val inventories: MutableList<Inventory>,
) : RecyclerView.Adapter<ListInventoryAdapter.DishViewHolder>()
{
    var onItemSelected: ((Inventory) -> Unit)? = null

    inner class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dishName: TextView = itemView.findViewById<TextView>(R.id.txtDishNameDetail)
        val dishPrice: TextView = itemView.findViewById<TextView>(R.id.txtDishPriceDetail)
        val dishImage: ImageView = itemView.findViewById<ImageView>(R.id.imgDishDetail)
        val dishColor: MaterialCardView = itemView.findViewById<MaterialCardView>(R.id.colorDishDetail)

        init {
            itemView.setOnClickListener {
                val selected = inventories[adapterPosition]
                onItemSelected?.invoke(selected)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DishViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_inventory, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DishViewHolder,
        position: Int
    ) {
        val inventory = inventories[position]

        holder.dishName.text = inventory.InventoryName.toString()
        holder.dishPrice.text = buildString {
            append("Giá bán: ")
            append(FormatDisplay.formatNumber(inventory.Price.toString()))
        }
        holder.dishColor.backgroundTintList = ColorStateList.valueOf(inventory.Color.toColorInt())
        holder.dishImage.setImageDrawable(ImageHelper.getDrawableImageFromAssets(context,inventory.IconFileName))
    }

    override fun getItemCount(): Int {
        return inventories.size
    }
}