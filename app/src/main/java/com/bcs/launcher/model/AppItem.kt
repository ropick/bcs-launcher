package com.bcs.launcher.model

data class AppItem(
    val id: String,
    val label: String,
    val iconRes: Int,
    val packageName: String? = null, // for installed/external apps
    val action: Action = Action.INTERNAL
) {
    enum class Action { INTERNAL, EXTERNAL, BCS_TV, YOUTUBE, YOUTUBE_MUSIC, FILES, MEDIA, APP_DRAWER, SETTINGS }
}
