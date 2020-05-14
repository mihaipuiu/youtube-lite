package com.mihaipuiu.youtubelite.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mihaipuiu.youtubelite.R
import com.mihaipuiu.youtubelite.adapters.FavoriteVideosAdapter
import com.mihaipuiu.youtubelite.database.FavoriteVideosDb
import com.mihaipuiu.youtubelite.models.FavoriteVideo
import com.mihaipuiu.youtubelite.models.Video
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: FavoritesViewModel
    lateinit var videos: ArrayList<FavoriteVideo>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoritesViewModel =
            ViewModelProviders.of(this).get(FavoritesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)
        val db = FavoriteVideosDb.getInstance(requireContext())

        videos = ArrayList()
        val adapter = FavoriteVideosAdapter(videos, db)
        val rvVideos = root.findViewById<View>(R.id.recyclerview_favoritevideos) as RecyclerView
        rvVideos.adapter = adapter
        rvVideos.layoutManager = LinearLayoutManager(activity)

        CoroutineScope(Dispatchers.Main).launch {
            val task = async(Dispatchers.IO) {
                db.favoriteVideoDao().getAll()
            }

            val favoriteVideos = task.await()
            videos.clear()
            favoriteVideos.toCollection(videos)

            adapter.notifyDataSetChanged()
        }

        return root
    }
}
