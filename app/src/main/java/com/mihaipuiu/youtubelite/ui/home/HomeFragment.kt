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
import com.mihaipuiu.youtubelite.adapters.VideosAdapter
import com.mihaipuiu.youtubelite.models.Video
import com.mihaipuiu.youtubelite.services.YoutubeService
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var videos: ArrayList<Video>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        videos = ArrayList()
        val adapter = VideosAdapter(videos)
        val rvVideos = root.findViewById<View>(R.id.recyclerview_videos) as RecyclerView
        rvVideos.adapter = adapter
        rvVideos.layoutManager = LinearLayoutManager(activity)

        CoroutineScope(Dispatchers.Main).launch {
            val task = async(Dispatchers.IO) {
                YoutubeService().getMostPopularVideos()
            }

            val newVideos = task.await()
            videos.clear()
            videos.addAll(newVideos)

            adapter.notifyDataSetChanged()
        }

        return root
    }
}
