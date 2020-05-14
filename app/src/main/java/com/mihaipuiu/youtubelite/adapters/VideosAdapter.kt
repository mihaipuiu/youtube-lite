package com.mihaipuiu.youtubelite.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mihaipuiu.youtubelite.R
import com.mihaipuiu.youtubelite.models.FavoriteVideo
import com.mihaipuiu.youtubelite.models.Video
import com.mihaipuiu.youtubelite.database.FavoriteVideosDb
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class VideosAdapter(private val mVideos: List<Video>) :
    RecyclerView.Adapter<VideosAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var db: FavoriteVideosDb

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val videoThumbnailImageButton = itemView.findViewById<ImageView>(R.id.video_thumbnail)
        val videoTitleTextView = itemView.findViewById<TextView>(R.id.video_title_text)
        val videoPlayButton = itemView.findViewById<ImageButton>(R.id.play_video_button)
        val addToFavoritesButton = itemView.findViewById<ImageButton>(R.id.add_favorite_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosAdapter.ViewHolder {
        this.context = parent.context
        this.db = FavoriteVideosDb.getInstance(this.context)
        val inflater = LayoutInflater.from(this.context)
        val contactView = inflater.inflate(R.layout.item_video, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(viewHolder: VideosAdapter.ViewHolder, position: Int) {
        val video: Video = mVideos.get(position)

        val imageView = viewHolder.videoThumbnailImageButton
        if (video.thumbnailUrl.isNotEmpty()) {
            Picasso.get().load(video.thumbnailUrl).into(imageView)
        }

        val textView = viewHolder.videoTitleTextView
        textView.text = video.title

        val playButton = viewHolder.videoPlayButton

        val addFavoriteButton = viewHolder.addToFavoritesButton
        addFavoriteButton.setOnClickListener(View.OnClickListener {

            if (addFavoriteButton.tag == 1) {
                // means that video is already added to favorites so we remove it (toggle)
                CoroutineScope(Dispatchers.Main).launch {
                    val task = async(Dispatchers.IO) {
                        val favoriteVideo = FavoriteVideo(video.id, video.title, video.thumbnailUrl)
                        db.favoriteVideoDao().remove(favoriteVideo)
                    }

                    task.await()

                    Toast.makeText(it.context, "Removed from favorites: " + video.title, Toast.LENGTH_SHORT).show()
                    addFavoriteButton.setImageResource(android.R.drawable.star_big_off)
                    addFavoriteButton.tag = null
                }
            } else if (addFavoriteButton.tag == null) {
                // means that video is not added to favorites so we add it (toggle)
                CoroutineScope(Dispatchers.Main).launch {
                    val task = async(Dispatchers.IO) {
                        val favoriteVideo = FavoriteVideo(video.id, video.title, video.thumbnailUrl)
                        db.favoriteVideoDao().add(favoriteVideo)
                    }

                    task.await()

                    Toast.makeText(it.context, "Added to favorites: " + video.title, Toast.LENGTH_SHORT).show()
                    addFavoriteButton.setImageResource(android.R.drawable.star_big_on)
                    addFavoriteButton.tag = 1
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return mVideos.size
    }
}