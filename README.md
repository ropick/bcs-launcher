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
