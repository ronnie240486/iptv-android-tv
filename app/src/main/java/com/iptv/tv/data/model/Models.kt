package com.iptv.tv.data.model

data class ServerConfig(
    val serverUrl: String = "",
    val username: String = "",
    val password: String = "",
    val type: ConnectionType = ConnectionType.XTREAM,
    val m3uUrl: String = ""
)

enum class ConnectionType { XTREAM, M3U }

data class Channel(
    val id: String,
    val name: String,
    val streamUrl: String,
    val logo: String = "",
    val categoryId: String = "",
    val categoryName: String = "",
    val epgChannelId: String = ""
)

data class Movie(
    val id: String,
    val name: String,
    val streamUrl: String,
    val cover: String = "",
    val categoryId: String = "",
    val categoryName: String = "",
    val rating: String = "",
    val year: String = "",
    val plot: String = "",
    val genre: String = "",
    val duration: String = ""
)

data class Series(
    val id: String,
    val name: String,
    val cover: String = "",
    val categoryId: String = "",
    val categoryName: String = "",
    val rating: String = "",
    val year: String = "",
    val plot: String = ""
)

data class Episode(
    val id: String,
    val title: String,
    val streamUrl: String,
    val season: Int,
    val episode: Int,
    val cover: String = ""
)

data class Category(
    val id: String,
    val name: String
)

data class EPGProgram(
    val channelId: String,
    val title: String,
    val description: String = "",
    val startTime: Long,
    val endTime: Long
) {
    val isLive: Boolean get() {
        val now = System.currentTimeMillis()
        return now in startTime..endTime
    }
    val progress: Float get() {
        val now = System.currentTimeMillis()
        return if (now < startTime) 0f
        else if (now > endTime) 1f
        else (now - startTime).toFloat() / (endTime - startTime)
    }
}
