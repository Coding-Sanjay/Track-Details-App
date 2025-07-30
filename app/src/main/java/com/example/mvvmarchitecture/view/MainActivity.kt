package com.example.mvvmarchitecture.view

import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.viewmodel.TrackViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val viewModel: TrackViewModel by viewModels()
    private lateinit var recyclerViewContainer : RecyclerView
    private lateinit var adapter : TrackAdapter
    private val tag = "Print Statements"
    private var isScrolling = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        recyclerViewContainer = findViewById<RecyclerView>(R.id.rvContainer)

        adapter = TrackAdapter(mutableListOf())
        recyclerViewContainer.adapter = adapter

        viewModel.trackList.observe(this@MainActivity) { list ->
            adapter.addTracks(list)
        }

        lifecycleScope.launch {
            Log.d(tag, "fetch")

            withContext(Dispatchers.IO) {
                viewModel.fetchData()
            }

            recyclerViewContainer.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) isScrolling = true
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount

                    val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
                    val shouldPaginate = isAtLastItem && isScrolling

                    if (shouldPaginate) {
                        viewModel.fetchData()
                        isScrolling = false
                    }
                }
            })
        }

    }
}



