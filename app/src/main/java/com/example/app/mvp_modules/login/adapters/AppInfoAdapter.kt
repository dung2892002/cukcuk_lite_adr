package com.example.app.mvp_modules.login.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.app.R

class AppInfoAdapter(context: Context, private val items: List<String>)
    : ArrayAdapter<String>(context,R.layout.item_app_info,items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_app_info, parent, false)

        val textView = view.findViewById<TextView>(R.id.txtInfo)
        textView.text = items[position]

        return view
    }
}