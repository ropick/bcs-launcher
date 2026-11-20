package com.bcs.launcher.media

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bcs.launcher.R
import com.bumptech.glide.Glide

class ImageViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)
        val path = intent.getStringExtra("path")
        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }
        if (!path.isNullOrBlank()) {
            Glide.with(this).load(Uri.parse(path)).into(findViewById<ImageView>(R.id.imgView))
        }
    }
}
