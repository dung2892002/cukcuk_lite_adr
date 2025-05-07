package com.example.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.app.R

class AppInfoAdapter(context: Context, private val items: List<String>)
    : ArrayAdapter<String>(context,R.layout.app_info_item,items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.app_info_item, parent, false)

        val textView = view.findViewById<TextView>(R.id.txtInfo)
        textView.text = items[position]

        return view
    }
}