package com.bcs.launcher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bcs.launcher.apps.AppDrawerActivity
import com.bcs.launcher.bcs.BcsTvActivity
import com.bcs.launcher.files.FileManagerActivity
import com.bcs.launcher.media.MediaPlayerActivity
import com.bcs.launcher.model.AppItem
import com.bcs.launcher.settings.SettingsActivity
import com.bcs.launcher.ui.AppsAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private val timeRunnable = object : Runnable {
        override fun run() {
            findViewById<TextView>(R.id.txtTime).text =
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            handler.postDelayed(this, 1000 * 30)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler.post(timeRunnable)

        val items = listOf(
            AppItem("bcs_tv", getString(R.string.bcs_tv), R.drawable.ic_bcs_tv, action = AppItem.Action.BCS_TV),
            AppItem("youtube", getString(R.string.youtube), R.drawable.ic_youtube, action = AppItem.Action.YOUTUBE),
            AppItem("youtube_music", getString(R.string.youtube_music), R.drawable.ic_ytmusic, action = AppItem.Action.YOUTUBE_MUSIC),
            AppItem("file_manager", getString(R.string.file_manager), R.drawable.ic_files, action = AppItem.Action.FILES),
            AppItem("media_player", getString(R.string.media_player), R.drawable.ic_media, action = AppItem.Action.MEDIA),
            AppItem("app_drawer", getString(R.string.app_drawer), R.drawable.ic_apps, action = AppItem.Action.APP_DRAWER),
            AppItem("settings", getString(R.string.settings), R.drawable.ic_settings, action = AppItem.Action.SETTINGS)
        )

        val recycler = findViewById<RecyclerView>(R.id.recyclerApps)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = AppsAdapter(items) { item -> handle(item) }

        findViewById<Button>(R.id.btnHero).setOnClickListener {
            startActivity(Intent(this, BcsTvActivity::class.java))
        }

        val quick = findViewById<LinearLayout>(R.id.quickCats)
        listOf("TV Lokal", "K-Drama", "Olahraga", "Musik").forEach { name ->
            val b = Button(this).apply {
                text = name
                isFocusable = true
                setOnClickListener { startActivity(Intent(this@MainActivity, BcsTvActivity::class.java)) }
            }
            quick.addView(b)
        }
    }

    private fun handle(item: AppItem) {
        try {
            when (item.action) {
                AppItem.Action.BCS_TV -> startActivity(Intent(this, BcsTvActivity::class.java))
                AppItem.Action.YOUTUBE -> openExternal("com.google.android.youtube", "https://www.youtube.com")
                AppItem.Action.YOUTUBE_MUSIC -> openExternal("com.google.android.apps.youtube.music", "https://music.youtube.com")
                AppItem.Action.FILES -> startActivity(Intent(this, FileManagerActivity::class.java))
                AppItem.Action.MEDIA -> startActivity(Intent(this, MediaPlayerActivity::class.java))
                AppItem.Action.APP_DRAWER -> startActivity(Intent(this, AppDrawerActivity::class.java))
                AppItem.Action.SETTINGS -> startActivity(Intent(this, SettingsActivity::class.java))
                else -> {}
            }
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.open_failed), Toast.LENGTH_SHORT).show()
        }
    }

    private fun openExternal(pkg: String, webUrl: String) {
        val pm = packageManager
        val candidates = listOf(pkg, "$pkg.tv", "com.google.android.youtube.tv", "com.google.android.apps.youtube.music")
        var launch: Intent? = null
        for (p in candidates) {
            launch = pm.getLaunchIntentForPackage(p)
            if (launch != null) break
        }
        if (launch != null) {
            startActivity(launch)
        } else {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeRunnable)
    }
}
