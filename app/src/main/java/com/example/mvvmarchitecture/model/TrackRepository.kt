package com.example.mvvmarchitecture.model

import android.util.Log
import com.example.mvvmarchitecture.model.remote.ApiService

class TrackRepository() {
    private val tag = "Print Statements"
    fun fetchData() : List<Track> {
        Log.d(tag, "repo start")
        return ApiService.fetchData()
    }
}

