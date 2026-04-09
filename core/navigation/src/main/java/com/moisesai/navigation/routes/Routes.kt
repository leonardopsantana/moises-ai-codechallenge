package com.moisesai.navigation.routes

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object Routes {
    const val HOME = "home"
    const val ALBUM = "album/{albumId}"
    const val ALBUM_DETAIL = "album"
    const val SPLASH = "splash"

    const val PLAYER = "player?trackIds={trackIds}&currentTrackId={currentTrackId}"

    fun playerRoute(trackIds: List<Long>, currentTrackId: Long): String {
        val idsString = trackIds.joinToString(",")
        val encodedIds = URLEncoder.encode(idsString, StandardCharsets.UTF_8.toString())
        return "player?trackIds=$encodedIds&currentTrackId=$currentTrackId"
    }
}