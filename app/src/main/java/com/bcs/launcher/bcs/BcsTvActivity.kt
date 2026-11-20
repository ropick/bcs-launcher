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
        "TV Lokal", "Film & Drama", "Olahraga", "Musik",
        "Ilmu Pengetahuan", "Berita", "Anak", "Lainnya"
    )

    /**
     * Daftar kanal resmi / free-to-air yang dipublikasikan langsung oleh
     * broadcaster atau mitra resminya (bukan re-stream / konten bajakan).
     * Beberapa kanal mungkin geo-restricted.
     */
    private val channels = listOf(
        // ---- TV Lokal Indonesia (sumber resmi: TVRI & Metcom/Metro TV) ----
        Channel("Metro TV", "TV Lokal", "https://edge.medcom.id/live-edge/smil:metro.smil/playlist.m3u8"),
        Channel("TVRI Nasional", "TV Lokal", "https://ott-balancer.tvri.go.id/live/eds/Nasional/hls/Nasional.m3u8"),
        Channel("TVRI World", "TV Lokal", "https://ott-balancer.tvri.go.id/live/eds/TVRIWorld/hls/TVRIWorld.m3u8"),
        Channel("TVRI Sport HD", "TV Lokal", "https://ott-balancer.tvri.go.id/live/eds/SportHD/hls/SportHD.m3u8"),
        Channel("TVRI Jakarta", "TV Lokal", "https://ott-balancer.tvri.go.id/live/eds/DKI/hls/DKI.m3u8"),
        Channel("TVRI Jawa Barat", "TV Lokal", "https://ott-balancer.tvri.go.id/live/eds/Jabar/hls/Jabar.m3u8"),
        Channel("TVRI Jawa Tengah", "TV Lokal", "https://ott-balancer.tvri.go.id/live/eds/Jateng/hls/Jateng.m3u8"),
        Channel("TVRI Jawa Timur", "TV Lokal", "https://ott-balancer.tvri.go.id/live/eds/Jatim/hls/Jatim.m3u8"),
        Channel("TVRI Yogyakarta", "TV Lokal", "https://ott-balancer.tvri.go.id/live/eds/Jogjakarta/hls/Jogjakarta.m3u8"),
        Channel("TVRI Bali", "TV Lokal", "https://ott-balancer.tvri.go.id/live/eds/Bali/hls/Bali.m3u8"),

        // ---- Film & Drama ----
        Channel("AsianCrush", "Film & Drama", "https://linear-900.frequency.stream/dist/cineverse/900/hls/master/playlist.m3u8"),
        Channel("Bloomberg Originals", "Film & Drama", "https://bloomberg.com/media-manifest/streams/qt.m3u8"),

        // ---- Olahraga ----
        Channel("Red Bull TV", "Olahraga", "https://rbmn-live.akamaized.net/hls/live/590964/BoRB-AT/master.m3u8"),

        // ---- Musik (radio internet resmi, didukung donasi) ----
        Channel("Radio Paradise (AAC 320)", "Musik", "https://stream.radioparadise.com/aac-320"),
        Channel("SomaFM - Groove Salad", "Musik", "https://ice1.somafm.com/groovesalad-128-mp3"),

        // ---- Ilmu Pengetahuan & Dokumenter ----
        Channel("BBC Earth", "Ilmu Pengetahuan", "https://amg00793-amg00793c6-xumo-us-2669.playouts.now.amagi.tv/BBCStudios-BBCEarthA-hls/playlist.m3u8"),
        Channel("NASA TV Public", "Ilmu Pengetahuan", "https://ntv1.akamaized.net/hls/live/2014075/NASA-NTV1-HLS/master.m3u8"),
        Channel("NASA TV Media", "Ilmu Pengetahuan", "https://ntv2.akamaized.net/hls/live/2013923/NASA-NTV2-HLS/master.m3u8"),
        Channel("NatureTime", "Ilmu Pengetahuan", "https://bamusa-naturetime-emea-eng-rakuten.amagi.tv/playlist.m3u8"),
        Channel("PBS Create", "Ilmu Pengetahuan", "https://create.lls.pbs.org/index.m3u8"),
        Channel("CGTN Documentary", "Ilmu Pengetahuan", "https://amg00405-rakutentv-cgtndocumentary-rakuten-0ql8j.amagi.tv/master.m3u8"),
        Channel("CNA Originals", "Ilmu Pengetahuan", "https://amg01082-cna-amg01082c1-rlaxx-us-11304.playouts.now.amagi.tv/playlist.m3u8"),
        Channel("Stingray Naturescape", "Ilmu Pengetahuan", "https://lotus.stingray.com/manifest/naturescape-a003-montreal/samsungtvplus/master.m3u8"),
        Channel("Love The Planet", "Ilmu Pengetahuan", "https://amg01821-lovetvchannels-lovetheplanetuksamsung-samsunguk-apopw.amagi.tv/playlist/amg01821-lovetvchannels-lovetheplanetuksamsung-samsunguk/playlist.m3u8"),
        Channel("Discovering China", "Ilmu Pengetahuan", "https://fastlive.cctvplus.com/out/v1/8442c3bfd4c64a3984389328f6087879/index.m3u8"),
        Channel("China Travel", "Ilmu Pengetahuan", "https://fastlive.cctvplus.com/out/v1/ca6f9297b7314a63959435028af287fc/index.m3u8"),

        // ---- Berita ----
        Channel("DW English", "Berita", "https://dwamdstream102.akamaized.net/hls/live/2015525/dwstream102/index.m3u8"),
        Channel("Al Jazeera English", "Berita", "https://live-hls-apps-aje-fa.getaj.net/AJE/index.m3u8"),
        Channel("France 24 English", "Berita", "https://live.france24.com/hls/live/2037218-b/F24_EN_HI_HLS/master_5000.m3u8"),
        Channel("CNA", "Berita", "https://d2e1asnsl7br7b.cloudfront.net/7782e205e72f43aeb4a48ec97f66ebbe/index.m3u8"),
        Channel("NHK World-Japan", "Berita", "https://masterpl.hls.nhkworld.jp/hls/w/live/smarttv.m3u8"),
        Channel("Bloomberg TV", "Berita", "https://bloomberg.com/media-manifest/streams/us.m3u8"),

        // ---- Anak ----
        Channel("Moonbug Kids", "Anak", "https://moonbug-rokuus.amagi.tv/playlist.m3u8"),
        Channel("ZooMoo", "Anak", "https://zoomoo-samsungau.amagi.tv/playlist.m3u8"),

        // ---- Lainnya ----
        Channel("Tastemade", "Lainnya", "https://rakutenaa-tm-intl-aus-rakuten-eu-n1gtg.amagi.tv/playlist/rakutenAA-tm-intl-aus-rakuten-eu/playlist.m3u8"),
        Channel("Arirang TV", "Lainnya", "https://amdlive-ch01-ctnd-com.akamaized.net/arirang_1ch/smil:arirang_1ch.smil/playlist.m3u8"),
        Channel("CGTN", "Lainnya", "https://english-livebkali.cgtn.com/live/encgtn.m3u8"),
        Channel("RTHK 31", "Lainnya", "https://rthktv31-live.akamaized.net/hls/live/2036818/RTHKTV31/master.m3u8"),
        Channel("CCTV-1", "Lainnya", "https://ldncctvwbcdali.v.myalicdn.com/ldncctvwbcd/cdrmldcctv1_1/index.m3u8"),
        Channel("CCTV-13", "Lainnya", "https://ldncctvwbcdali.v.myalicdn.com/ldncctvwbcd/cdrmldcctv13_1/index.m3u8")
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
