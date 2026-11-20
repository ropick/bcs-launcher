package com.bcs.launcher.apps

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bcs.launcher.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppDrawerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_drawer)
        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }

        val recycler = findViewById<RecyclerView>(R.id.recyclerApps)
        recycler.layoutManager = GridLayoutManager(this, 6)

        lifecycleScope.launch {
            val apps = withContext(Dispatchers.IO) { loadApps() }
            recycler.adapter = InstalledAppAdapter(apps) { info ->
                try {
                    val launch = packageManager.getLaunchIntentForPackage(info.packageName)
                    if (launch != null) startActivity(launch) else Toast.makeText(this@AppDrawerActivity, R.string.open_failed, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AppDrawerActivity, R.string.open_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadApps(): List<ApplicationInfo> {
        val pm = packageManager
        return pm.getInstalledApplications(0)
            .filter { it.packageName != packageName }
            .filter { pm.getLaunchIntentForPackage(it.packageName) != null }
            .sortedBy { pm.getApplicationLabel(it).toString().lowercase() }
    }
}

class InstalledAppAdapter(
    private val items: List<ApplicationInfo>,
    private val onClick: (ApplicationInfo) -> Unit
) : RecyclerView.Adapter<InstalledAppAdapter.VH>() {
    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val icon: ImageView = v.findViewById(R.id.imgIcon)
        val label: TextView = v.findViewById(R.id.txtLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val info = items[position]
        holder.icon.setImageDrawable(info.loadIcon(holder.itemView.context.packageManager))
        holder.label.text = info.loadLabel(holder.itemView.context.packageManager)
        holder.itemView.setOnClickListener { onClick(info) }
    }

    override fun getItemCount() = items.size
}
