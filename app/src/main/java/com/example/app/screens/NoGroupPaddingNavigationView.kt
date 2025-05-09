package com.example.app.screens

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
class NoGroupPaddingNavigationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : NavigationView(context, attrs) {

    override fun inflateMenu(resId: Int) {
        super.inflateMenu(resId)

        post {
            removeGroupPadding(this)
        }
    }

    private fun removeGroupPadding(viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)

            if (child is ViewGroup) {
                val left = child.paddingLeft
                val right = child.paddingRight

                child.setPadding(left, 0, right, 0)
                val params = child.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = 0
                params.bottomMargin = 0
                child.layoutParams = params
                removeGroupPadding(child)
            }
        }
    }
}
