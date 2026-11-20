# BCS Launcher

Launcher untuk Android TV / STB. Menyediakan:
- BCS TV (TV lokal, film & drama, olahraga, musik, ilmu pengetahuan, berita, anak)
- YouTube
- YouTube Music
- File Manager
- Media Player (video/audio/gambar)
- App Drawer (semua aplikasi terpasang)
- Pengaturan

## Build
1. Buka folder ini di Android Studio (Hedgehog+).
2. Pastikan Android SDK 34 terpasang.
3. Sync Gradle, lalu Run/Build APK.

Catatan: project menggunakan AGP 8.5.2, Kotlin 2.0.20, Gradle 8.9.
Jika belum ada gradle wrapper jar, jalankan `gradle wrapper` atau gunakan Gradle terpasang.

## BCS TV - Sumber kanal
Daftar kanal ada di `app/src/main/java/com/bcs/launcher/bcs/BcsTvActivity.kt`.
Semua URL adalah stream HLS/audio resmi yang dipublikasikan sendiri oleh broadcaster
atau mitra free-TV resminya (TVRI, DW, France 24, Al Jazeera, CNA, NHK, Bloomberg,
BBC Earth, NASA, PBS, Red Bull TV, Radio Paradise, SomaFM, dll). Salinan playlist
M3U ada di `bcs-tv-channels.m3u`.

Catatan:
- Beberapa kanal mungkin hanya bisa diputar di wilayah tertentu (geo-restriction)
  atau sewaktu-waktu mengganti URL.
- Modul `media3-exoplayer-hls` wajib ada agar ExoPlayer bisa memutar `.m3u8`.
- Konten on-demand berlisensi (Netflix, Viu, Vidio, WeTV, dll.) sengaja tidak
  dimasukkan karena tidak menyediakan URL stream publik yang legal.

## BCS TV - TV lokal lengkap (playlist komunitas)

Kanal swasta seperti RCTI, SCTV, Indosiar, GTV, ANTV, tvOne, Trans TV dll. **tidak
menyediakan HLS publik resmi** (mereka pakai app OTT ber-login + DRM). Untuk
itu tersedia kategori **TV Lokal Komunitas** yang mengambil file M3U dari URL
saat aplikasi dibuka, jadi daftar kanal bisa diperbarui **tanpa rebuild APK**.

- Default: `https://raw.githubusercontent.com/iptv-org/iptv/master/streams/id.m3u`
- Ganti URL: **tahan lama** kategori `TV Lokal Komunitas` di Android TV, atau
  **klik 2x** kategori tersebut di preview web. Tombol "Reset" mengembalikan URL default.
- Muat ulang: ketuk teks status di bawah kategori (Android) / klik teks status (web).
- URL tersimpan di `SharedPreferences` (`bcs_tv / playlist_url`).
- Entri tanpa URL, duplikat, dan label `[Not 24/7]` / `[Geo-blocked]` / `(720p)`
  dibersihkan otomatis saat parsing.

Contoh playlist lain yang bisa ditempel:
```
https://raw.githubusercontent.com/iptv-org/iptv/master/streams/id.m3u
https://example.com/playlist.m3u
```

URL harus diawali `http://` atau `https://`.

Peringatan: kanal dari playlist komunitas umumnya adalah **re-stream pihak
ketiga**, bukan CDN resmi broadcaster. Kualitas dan ketersediaannya tidak
dijamin, beberapa geo-blocked, dan status hukumnya abu-abu. Kanal resmi
(TVRI/Metro TV) tetap tersedia di kategori `TV Lokal` sebagai fallback.

