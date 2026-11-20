package com.bcs.launcher.files

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bcs.launcher.R
import com.bcs.launcher.media.ImageViewerActivity
import com.bcs.launcher.media.MediaPlayerActivity
import java.io.File

class FileManagerActivity : AppCompatActivity() {

    private lateinit var currentDir: File
    private lateinit var recycler: RecyclerView
    private lateinit var txtPath: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_manager)

        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<View>(R.id.btnUp).setOnClickListener { goUp() }
        recycler = findViewById(R.id.recyclerFiles)
        txtPath = findViewById(R.id.txtPath)
        recycler.layoutManager = LinearLayoutManager(this)

        if (hasPermission()) {
            start(Environment.getExternalStorageDirectory())
        } else {
            requestPermission()
        }
    }

    private fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasPermission() && !::currentDir.isInitialized) {
            start(Environment.getExternalStorageDirectory())
        } else if (hasPermission() && ::currentDir.isInitialized) {
            load(currentDir)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            start(Environment.getExternalStorageDirectory())
        }
    }

    private fun start(dir: File) {
        currentDir = dir
        load(dir)
    }

    private fun load(dir: File) {
        currentDir = dir
        txtPath.text = dir.absolutePath
        val files = dir.listFiles()?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() })) ?: emptyList()
        recycler.adapter = FileAdapter(files) { f -> onFile(f) }
    }

    private fun goUp() {
        val parent = currentDir.parentFile
        if (parent != null && parent.canRead()) load(parent)
    }

    private fun onFile(f: File) {
        if (f.isDirectory) {
            load(f)
        } else {
            val mime = guessMime(f.name)
            when {
                mime.startsWith("video") || mime.startsWith("audio") -> {
                    val i = Intent(this, MediaPlayerActivity::class.java)
                    i.putExtra("url", Uri.fromFile(f).toString())
                    i.putExtra("title", f.name)
                    startActivity(i)
                }
                mime.startsWith("image") -> {
                    val i = Intent(this, ImageViewerActivity::class.java)
                    i.putExtra("path", Uri.fromFile(f).toString())
                    startActivity(i)
                }
                else -> Toast.makeText(this, f.name, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guessMime(name: String): String {
        val lower = name.lowercase()
        return when {
            lower.endsWith(".mp4") || lower.endsWith(".mkv") || lower.endsWith(".avi") || lower.endsWith(".mov") -> "video/*"
            lower.endsWith(".mp3") || lower.endsWith(".wav") || lower.endsWith(".aac") || lower.endsWith(".flac") -> "audio/*"
            lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".gif") || lower.endsWith(".webp") -> "image/*"
            else -> "application/octet-stream"
        }
    }
}

class FileAdapter(private val items: List<File>, private val onClick: (File) -> Unit) :
    RecyclerView.Adapter<FileAdapter.VH>() {
    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val icon: ImageView = v.findViewById(R.id.imgIcon)
        val name: TextView = v.findViewById(R.id.txtName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val f = items[position]
        holder.name.text = f.name
        holder.icon.setImageResource(if (f.isDirectory) android.R.drawable.ic_menu_close_clear_cancel else android.R.drawable.ic_menu_gallery)
        holder.itemView.setOnClickListener { onClick(f) }
    }

    override fun getItemCount() = items.size
}
