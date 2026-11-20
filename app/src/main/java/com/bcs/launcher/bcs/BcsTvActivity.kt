package com.bcs.launcher.bcs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bcs.launcher.R
import com.bcs.launcher.media.MediaPlayerActivity

class BcsTvActivity : AppCompatActivity() {

    private val categories = listOf(
        "TV Lokal Indonesia", "Drama Korea", "Drama China", "Olahraga", "Musik", "Berita", "Anak"
    )

    private val channels = listOf(
        Channel("RCTI", "TV Lokal Indonesia", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
        Channel("SCTV", "TV Lokal Indonesia", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
        Channel("Indosiar", "TV Lokal Indonesia", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"),
        Channel("Trans TV", "TV Lokal Indonesia", "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"),
        Channel("K-Drama 1", "Drama Korea", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"),
        Channel("K-Drama 2", "Drama Korea", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
        Channel("K-Drama 3", "Drama Korea", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
        Channel("C-Drama 1", "Drama China", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"),
        Channel("C-Drama 2", "Drama China", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"),
        Channel("Sport 1", "Olahraga", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
        Channel("Sport 2", "Olahraga", "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"),
        Channel("Music 1", "Musik", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
        Channel("Music 2", "Musik", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"),
        Channel("News 1", "Berita", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"),
        Channel("Kids 1", "Anak", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
    )

    private var selectedCategory = categories.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bcs_tv)

        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }

        val row = findViewById<LinearLayout>(R.id.categoryRow)
        categories.forEach { cat ->
            val btn = Button(this).apply {
                text = cat
                isFocusable = true
                setOnClickListener {
                    selectedCategory = cat
                    refresh()
                }
            }
            row.addView(btn)
        }

        findViewById<RecyclerView>(R.id.recyclerChannels).layoutManager = LinearLayoutManager(this)
        refresh()
    }

    private fun refresh() {
        val filtered = channels.filter { it.category == selectedCategory }
        val adapter = ChannelAdapter(filtered) { ch ->
            val i = Intent(this, MediaPlayerActivity::class.java)
            i.putExtra("url", ch.url)
            i.putExtra("title", ch.name)
            startActivity(i)
        }
        findViewById<RecyclerView>(R.id.recyclerChannels).adapter = adapter
    }
}

class ChannelAdapter(
    private val items: List<Channel>,
    private val onClick: (Channel) -> Unit
) : RecyclerView.Adapter<ChannelAdapter.VH>() {
    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val tv = TextView(parent.context).apply {
            textSize = 18f
            setPadding(24, 24, 24, 24)
            isFocusable = true
            isClickable = true
        }
        return VH(tv)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val ch = items[position]
        holder.title.text = ch.name
        holder.itemView.setOnClickListener { onClick(ch) }
    }

    override fun getItemCount() = items.size
}
