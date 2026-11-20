package com.bcs.launcher.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bcs.launcher.R
import com.bcs.launcher.model.AppItem

class AppsAdapter(
    private val items: List<AppItem>,
    private val onClick: (AppItem) -> Unit
) : RecyclerView.Adapter<AppsAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.imgIcon)
        val label: TextView = view.findViewById(R.id.txtLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.iconRes)
        holder.label.text = item.label
        holder.itemView.setOnClickListener { onClick(item) }
        holder.itemView.setOnKeyListener { _, _, event ->
            if (event.action == android.view.KeyEvent.ACTION_DOWN &&
                (event.keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER ||
                    event.keyCode == android.view.KeyEvent.KEYCODE_ENTER)
            ) {
                onClick(item)
                true
            } else false
        }
    }

    override fun getItemCount() = items.size
}
