package com.example.app.mvp_modules.sale.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.dto.InventorySelect
import com.example.app.utils.FormatDisplay
import com.example.app.utils.ImageHelper
import com.google.android.material.card.MaterialCardView

class ListSelectInventoryAdapter(
    private val context: Context,
    private val dishes: List<InventorySelect>
) : RecyclerView.Adapter<ListSelectInventoryAdapter.DishSelectViewHolder>()
{
    var onClickButtonAdd: ((InventorySelect, Int) -> Unit)? = null
    var onClickButtonSubtract: ((InventorySelect, Int) -> Unit)? = null
    var onClickButtonRemove: ((InventorySelect, Int) -> Unit)? = null
    var onClickOpenCalculator: ((InventorySelect, Int) -> Unit)? = null
    var onClickItem:((InventorySelect, Int) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    inner class DishSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemLayout: FrameLayout = itemView.findViewById<FrameLayout>(R.id.itemDishSelectLayout)

        val btnRemove: MaterialCardView = itemView.findViewById<MaterialCardView>(R.id.btnCheckQuantity)
        val btnAdd: MaterialCardView = itemView.findViewById<MaterialCardView>(R.id.btnAdd)
        val btnSubtract: MaterialCardView = itemView.findViewById<MaterialCardView>(R.id.btnSubtract)
        val btnCalculator: CardView = itemView.findViewById<MaterialCardView>(R.id.btnOpenCalculator)

        val editQuantity: LinearLayout = itemView.findViewById<LinearLayout>(R.id.groupEditQuantity)
        val dishColor: MaterialCardView = itemView.findViewById<MaterialCardView>(R.id.colorDishSelect)
        val dishImage: ImageView = itemView.findViewById<ImageView>(R.id.imgDishSelect)
        val dishName: TextView = itemView.findViewById<TextView>(R.id.txtDishNameSelect)
        val dishPrice: TextView = itemView.findViewById<TextView>(R.id.txtDishPriceSelect)
        val dishOrderQuantity: TextView = itemView.findViewById<TextView>(R.id.txtDishQuantitySelect)

        init {
            itemView.setOnClickListener {
                val selected = dishes[adapterPosition]
                onClickItem?.invoke(selected, adapterPosition)
                notifyItemChanged(adapterPosition)
            }

            btnRemove.setOnClickListener {
                val selected = dishes[adapterPosition]
                onClickButtonRemove?.invoke(selected, adapterPosition)
                notifyItemChanged(adapterPosition)
            }

            btnAdd.setOnClickListener {
                val selected = dishes[adapterPosition]
                onClickButtonAdd?.invoke(selected, adapterPosition)
                notifyItemChanged(adapterPosition)
            }

            btnSubtract.setOnClickListener {
                val selected = dishes[adapterPosition]
                onClickButtonSubtract?.invoke(selected, adapterPosition)
                notifyItemChanged(adapterPosition)
            }

            btnCalculator.setOnClickListener {
                val selected = dishes[adapterPosition]
                onClickOpenCalculator?.invoke(selected, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DishSelectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_inventory,null)
        return DishSelectViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DishSelectViewHolder,
        position: Int
    ) {
        val dishSelect = dishes[position]

        if (dishSelect.quantity != 0.0) {
            holder.editQuantity.visibility = View.VISIBLE
            holder.dishColor.visibility = View.GONE
            holder.btnRemove.visibility = View.VISIBLE
            val color = ContextCompat.getColor(holder.itemView.context, R.color.dish_selected)
            holder.itemLayout.setBackgroundColor(color)
        } else {
            holder.editQuantity.visibility = View.GONE
            holder.dishColor.visibility = View.VISIBLE
            holder.btnRemove.visibility = View.GONE
            holder.itemLayout.setBackgroundColor("#ffffffff".toColorInt())
        }

        holder.dishColor.backgroundTintList = ColorStateList.valueOf(dishSelect.inventory.Color.toColorInt())
        holder.dishImage.setImageDrawable(ImageHelper.getDrawableImageFromAssets(context,dishSelect.inventory.IconFileName))
        holder.dishName.text = dishSelect.inventory.InventoryName
        holder.dishPrice.text = FormatDisplay.formatNumber(dishSelect.inventory.Price.toString())
        holder.dishOrderQuantity.text = FormatDisplay.formatNumber(dishSelect.quantity.toString())


    }

    override fun getItemCount(): Int {
        return dishes.size
    }
}