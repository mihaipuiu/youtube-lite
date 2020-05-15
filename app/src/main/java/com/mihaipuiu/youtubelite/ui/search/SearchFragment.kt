package com.mihaipuiu.youtubelite.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mihaipuiu.youtubelite.R
import com.mihaipuiu.youtubelite.adapters.VideosAdapter
import com.mihaipuiu.youtubelite.database.FavoriteVideosDb
import com.mihaipuiu.youtubelite.models.FavoriteVideo
import com.mihaipuiu.youtubelite.models.Video
import com.mihaipuiu.youtubelite.services.YoutubeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchedVideos: ArrayList<Video>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        val db = FavoriteVideosDb.getInstance(requireContext())

        val searchView = root.findViewById<SearchView>(R.id.search_input)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (searchView.query.toString().isEmpty()) {
                    return false
                }

                CoroutineScope(Dispatchers.Main).launch {
                    val favVideos = db.favoriteVideoDao().getAll()
                    val favoredVideos = mutableMapOf<String, FavoriteVideo>()

                    for (i in favVideos.indices) {
                        val video = favVideos.get(i)
                        favoredVideos[video.id] = video
                    }

                    searchedVideos = ArrayList()
                    val adapter = VideosAdapter(searchedVideos, favoredVideos, db)
                    val rvVideos = root.findViewById<View>(R.id.recyclerview_searchedvideos) as RecyclerView
                    rvVideos.adapter = adapter
                    rvVideos.layoutManager = LinearLayoutManager(activity)

                    val task = async(Dispatchers.IO) {
                        YoutubeService().getSearchVideos(searchView.query.toString())
                    }

                    val newVideos = task.await()
                    searchedVideos.clear()
                    searchedVideos.addAll(newVideos)

                    adapter.notifyDataSetChanged()
                }


                return false
            }
        })

        return root
    }
}
