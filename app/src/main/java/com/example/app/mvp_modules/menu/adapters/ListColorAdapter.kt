package com.example.app.mvp_modules.menu.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R

class ListColorAdapter(
    private val context: Context,
    private val colors: List<String>,
    private val colorSelected: String)
    :RecyclerView.Adapter<ListColorAdapter.ColorViewHolder>(){

    var onColorSelected: ((String) -> Unit)? = null

    inner class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewColor: View = view.findViewById(R.id.viewColor)
        val imgCheck: ImageView = view.findViewById(R.id.imgCheckColor)

        init {
            view.setOnClickListener {
                onColorSelected?.invoke(colors[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val colorInt = colors[position].toColorInt()
        holder.viewColor.backgroundTintList = ColorStateList.valueOf(colorInt)

        if (colors[position] == colorSelected) {
            holder.imgCheck.visibility = View.VISIBLE
        }
        else {
            holder.imgCheck.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int {
        return colors.size
    }
}