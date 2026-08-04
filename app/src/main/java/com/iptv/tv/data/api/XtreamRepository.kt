package com.iptv.tv.data.api

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.JsonArray
import com.iptv.tv.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class XtreamRepository(private val context: Context) {

    private val SERVER_URL = stringPreferencesKey("server_url")
    private val USERNAME = stringPreferencesKey("username")
    private val PASSWORD = stringPreferencesKey("password")
    private val M3U_URL = stringPreferencesKey("m3u_url")
    private val CONN_TYPE = stringPreferencesKey("conn_type")

    val configFlow: Flow<ServerConfig> = context.dataStore.data.map { prefs ->
        ServerConfig(
            serverUrl = prefs[SERVER_URL] ?: "",
            username = prefs[USERNAME] ?: "",
            password = prefs[PASSWORD] ?: "",
            m3uUrl = prefs[M3U_URL] ?: "",
            type = if (prefs[CONN_TYPE] == "M3U") ConnectionType.M3U else ConnectionType.XTREAM
        )
    }

    suspend fun saveConfig(config: ServerConfig) {
        context.dataStore.edit { prefs ->
            prefs[SERVER_URL] = config.serverUrl
            prefs[USERNAME] = config.username
            prefs[PASSWORD] = config.password
            prefs[M3U_URL] = config.m3uUrl
            prefs[CONN_TYPE] = config.type.name
        }
    }

    suspend fun isConfigured(): Boolean {
        val config = configFlow.first()
        return (config.type == ConnectionType.XTREAM && config.serverUrl.isNotEmpty() && config.username.isNotEmpty())
            || (config.type == ConnectionType.M3U && config.m3uUrl.isNotEmpty())
    }

    private fun buildApi(baseUrl: String): XtreamApi {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()
        return Retrofit.Builder()
            .baseUrl(if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(XtreamApi::class.java)
    }

    suspend fun getLiveChannels(categoryId: String = ""): List<Channel> {
        val config = configFlow.first()
        val api = buildApi(config.serverUrl)
        return try {
            val result = api.getLiveStreams(config.username, config.password, categoryId = categoryId)
            result.mapNotNull { elem ->
                val obj = elem.asJsonObject
                val id = obj.get("stream_id")?.asString ?: return@mapNotNull null
                Channel(
                    id = id,
                    name = obj.get("name")?.asString ?: "",
                    streamUrl = "${config.serverUrl}/live/${config.username}/${config.password}/$id.m3u8",
                    logo = obj.get("stream_icon")?.asString ?: "",
                    categoryId = obj.get("category_id")?.asString ?: "",
                    epgChannelId = obj.get("epg_channel_id")?.asString ?: ""
                )
            }
        } catch (e: Exception) { emptyList() }
    }

    suspend fun getLiveCategories(): List<Category> {
        val config = configFlow.first()
        val api = buildApi(config.serverUrl)
        return try {
            val result = api.getLiveCategories(config.username, config.password)
            parseCategories(result)
        } catch (e: Exception) { emptyList() }
    }

    suspend fun getMovies(categoryId: String = ""): List<Movie> {
        val config = configFlow.first()
        val api = buildApi(config.serverUrl)
        return try {
            val result = api.getVodStreams(config.username, config.password, categoryId = categoryId)
            result.mapNotNull { elem ->
                val obj = elem.asJsonObject
                val id = obj.get("stream_id")?.asString ?: return@mapNotNull null
                Movie(
                    id = id,
                    name = obj.get("name")?.asString ?: "",
                    streamUrl = "${config.serverUrl}/movie/${config.username}/${config.password}/$id.mp4",
                    cover = obj.get("stream_icon")?.asString ?: "",
                    categoryId = obj.get("category_id")?.asString ?: "",
                    rating = obj.get("rating")?.asString ?: "",
                    year = obj.get("year")?.asString ?: "",
                    plot = obj.get("plot")?.asString ?: "",
                    genre = obj.get("genre")?.asString ?: ""
                )
            }
        } catch (e: Exception) { emptyList() }
    }

    suspend fun getMovieCategories(): List<Category> {
        val config = configFlow.first()
        val api = buildApi(config.serverUrl)
        return try {
            val result = api.getVodCategories(config.username, config.password)
            parseCategories(result)
        } catch (e: Exception) { emptyList() }
    }

    suspend fun getSeries(categoryId: String = ""): List<Series> {
        val config = configFlow.first()
        val api = buildApi(config.serverUrl)
        return try {
            val result = api.getSeries(config.username, config.password, categoryId = categoryId)
            result.mapNotNull { elem ->
                val obj = elem.asJsonObject
                val id = obj.get("series_id")?.asString ?: return@mapNotNull null
                Series(
                    id = id,
                    name = obj.get("name")?.asString ?: "",
                    cover = obj.get("cover")?.asString ?: "",
                    categoryId = obj.get("category_id")?.asString ?: "",
                    rating = obj.get("rating")?.asString ?: "",
                    year = obj.get("releaseDate")?.asString ?: "",
                    plot = obj.get("plot")?.asString ?: ""
                )
            }
        } catch (e: Exception) { emptyList() }
    }

    private fun parseCategories(arr: JsonArray): List<Category> =
        arr.mapNotNull { elem ->
            val obj = elem.asJsonObject
            val id = obj.get("category_id")?.asString ?: return@mapNotNull null
            Category(id = id, name = obj.get("category_name")?.asString ?: "")
        }
}
