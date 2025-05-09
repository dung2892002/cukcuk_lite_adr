package com.example.app.mvp_modules.menu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.utils.ImageHelper

class ListImageAdapter(
    private val context: Context,
    private val images: List<String>
) : RecyclerView.Adapter<ListImageAdapter.ImageViewHolder>()
{
    var onImageSelected: ((String) -> Unit)? = null

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewColor: View = view.findViewById(R.id.viewImage)

        init {
            view.setOnClickListener {
                onImageSelected?.invoke(images[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = ImageHelper.getDrawableImageFromAssets(context, images[position])
        holder.viewColor.findViewById<ImageView>(R.id.imgItem).setImageDrawable(image)
    }

    override fun getItemCount(): Int {
        return images.size
    }
}