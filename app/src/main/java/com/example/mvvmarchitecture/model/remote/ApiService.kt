package com.example.mvvmarchitecture.model.remote

import android.util.Log
import com.example.mvvmarchitecture.model.PlaylistTracksResponse
import com.example.mvvmarchitecture.model.RawTrack
import com.example.mvvmarchitecture.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.Base64

object ApiService {
    val client = OkHttpClient()
    val clientId = "15ad5557635b454b92497fed4387fe83"
    val clientSecret = "119cef62f73c4459bd182579c7d802a4"
    var accessToken : String? = null
    private var playListId = "75wi76gHtFUUYspvu4iFhX"
    private var limit = 10
    private var offset = 5

    private val tag = "Print Statements"

    // Get Access Code for the Authentication
    private fun getSpotifyAccessToken() : String? {
        val credentials = "$clientId:$clientSecret"
        val base64Encode = "Basic " + Base64.getEncoder().encodeToString(credentials.toByteArray())
        val requestBody = "grant_type=client_credentials".toRequestBody()

        val request = Request.Builder()
            .url("https://accounts.spotify.com/api/token")
            .post(requestBody)
            .addHeader("Authorization", base64Encode)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                return null
            }
            val jsonObject = JSONObject(response.body?.string())
            return jsonObject.getString("access_token")
        }
    }

    // Fetch the data form the Server
    fun fetchData() : List<Track> {

        if (accessToken == null) {
            runBlocking(Dispatchers.IO) {
                accessToken = getSpotifyAccessToken()
            }
        }

        Log.d("print value", accessToken.toString())

        val url = "https://api.spotify.com/v1/playlists/$playListId/tracks?limit=$limit&offset=$offset"

        Log.d("offset", offset.toString())
        Log.d("offset", url)
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        offset += limit

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonParser = Json {
                    ignoreUnknownKeys = true
                }

                try {
                    val playlistTracksResponse = jsonParser.decodeFromString<PlaylistTracksResponse>(responseBody!!)
                    val tracks: List<Track> = playlistTracksResponse.items.map { it.track.toTrack() }

                    return tracks
                } catch (e: Exception) {
                    e.printStackTrace()
                    return emptyList()
                }
            }
        }
        return emptyList()
    }

    fun RawTrack.toTrack(): Track {
        return Track(
            albumType = album.albumType,
            songName = name,
            releaseDate = album.releaseDate,
            artists = artists,
            durationInMillis = durationInMillis,
            imageUrl = album.images.firstOrNull()?.url ?: ""
        )
    }
}