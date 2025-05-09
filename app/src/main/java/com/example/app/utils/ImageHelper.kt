package com.example.app.utils

import android.content.Context
import android.graphics.drawable.Drawable

object ImageHelper {
    fun getDrawableImageFromAssets(context: Context, imgName: String) : Drawable {
        val assetManager = context.assets
        val inputStream = assetManager.open("icon_default/$imgName") // thêm thư mục con
        val drawable = Drawable.createFromStream(inputStream, null)

        return drawable!!
    }
}