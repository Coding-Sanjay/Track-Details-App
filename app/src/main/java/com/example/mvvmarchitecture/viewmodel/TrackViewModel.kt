package com.example.mvvmarchitecture.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecture.model.remote.ApiService
import com.example.mvvmarchitecture.model.Track
import com.example.mvvmarchitecture.model.TrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackViewModel() : ViewModel() {
    private val tag = "Print Statements"
    private val trackRepository = TrackRepository()
    private val _trackList = MutableLiveData<List<Track>>()
    val trackList : LiveData<List<Track>>
        get() = _trackList

    private val currentTrack = mutableListOf<Track>()
    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(tag, "toStart")
            val tracks = trackRepository.fetchData()
            currentTrack.addAll(tracks)
            _trackList.postValue(currentTrack)
            Log.d(tag, tracks.toString())
        }
    }
}