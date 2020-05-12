package com.mihaipuiu.youtubelite.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mihaipuiu.youtubelite.R
import com.mihaipuiu.youtubelite.adapters.YoutubeDataAdapter
import com.mihaipuiu.youtubelite.services.YoutubeService
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        GlobalScope.launch {
            val youtubeService = YoutubeService()
            val videos = youtubeService.getMostPopularVideos()
            //val x = 0
        }

        viewManager = LinearLayoutManager(activity)
        //viewAdapter = YoutubeDataAdapter(myDataset)

//        recyclerView = root.findViewById<RecyclerView>(R.id.list_home).apply {
//            layoutManager = viewManager
//            adapter = viewAdapter
//        }

        return root
    }
}
