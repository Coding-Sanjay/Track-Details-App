package com.example.mvvmarchitecture.model


data class Track(
    val albumType: String,
    val songName: String,
    val releaseDate: String,
    val durationInMillis: Int,
    val artists: List<Artist>,
    val imageUrl: String
)