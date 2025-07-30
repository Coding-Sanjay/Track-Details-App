package com.example.mvvmarchitecture.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistTracksResponse(
    val items: List<TrackItem>,
    val next : String,
)
@Serializable
data class TrackItem(
    val track: RawTrack
)
@Serializable
data class RawTrack (
    val name: String,
    val album: Album,
    val artists: List<Artist>,
    @SerialName("duration_ms") val durationInMillis: Int,
)
@Serializable
data class Album(
    @SerialName("album_type") val albumType: String,
    @SerialName("release_date") val releaseDate: String,
    val images: List<Image>
)
@Serializable
data class Image(
    val url: String
)
@Serializable
data class Artist(
    val name: String
)
