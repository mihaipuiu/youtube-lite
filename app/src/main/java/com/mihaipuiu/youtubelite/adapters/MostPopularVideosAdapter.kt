package com.mihaipuiu.youtubelite.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.mihaipuiu.youtubelite.R
import com.mihaipuiu.youtubelite.database.FavoriteVideosDb
import com.mihaipuiu.youtubelite.models.FavoriteVideo
import com.mihaipuiu.youtubelite.models.Video
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MostPopularVideosAdapter(private val mVideos: List<Video>, private val mFavoredVideos: MutableMap<String, FavoriteVideo>, private val db: FavoriteVideosDb) :
    RecyclerView.Adapter<MostPopularVideosAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val videoThumbnailImageButton = itemView.findViewById<ImageView>(R.id.video_thumbnail)
        val videoTitleTextView = itemView.findViewById<TextView>(R.id.video_title_text)
        val videoPlayButton = itemView.findViewById<ImageButton>(R.id.play_video_button)
        val addToFavoritesButton = itemView.findViewById<ImageButton>(R.id.add_favorite_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostPopularVideosAdapter.ViewHolder {
        this.context = parent.context
        val inflater = LayoutInflater.from(this.context)
        val contactView = inflater.inflate(R.layout.item_video, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(viewHolder: MostPopularVideosAdapter.ViewHolder, position: Int) {
        val video: Video = mVideos.get(position)

        val imageView = viewHolder.videoThumbnailImageButton
        if (video.thumbnailUrl.isNotEmpty()) {
            Picasso.get().load(video.thumbnailUrl).into(imageView)
        }

        val textView = viewHolder.videoTitleTextView
        textView.text = video.title

        val playButton = viewHolder.videoPlayButton

        playButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(android.content.Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.youtube.com/watch?v=" + video.id)
            this.context.startActivity(intent)
        })

        val addFavoriteButton = viewHolder.addToFavoritesButton

        if (mFavoredVideos.containsKey(video.id)) {
            addFavoriteButton.tag == 1
            addFavoriteButton.setImageResource(android.R.drawable.star_big_on)
        }

        addFavoriteButton.setOnClickListener(View.OnClickListener {

            if (mFavoredVideos.containsKey(video.id)) {
                // means that video is already added to favorites so we remove it (toggle)
                CoroutineScope(Dispatchers.Main).launch {
                    val task = async(Dispatchers.IO) {
                        val favoriteVideo = FavoriteVideo(video.id, video.title, video.thumbnailUrl)
                        db.favoriteVideoDao().remove(favoriteVideo)
                        mFavoredVideos.remove(video.id)
                    }

                    task.await()

                    Toast.makeText(it.context, "Removed from favorites: " + video.title, Toast.LENGTH_SHORT).show()
                    addFavoriteButton.setImageResource(android.R.drawable.star_big_off)
                    addFavoriteButton.tag = null
                }
            } else {
                // means that video is not added to favorites so we add it (toggle)
                CoroutineScope(Dispatchers.Main).launch {
                    val task = async(Dispatchers.IO) {
                        val favoriteVideo = FavoriteVideo(video.id, video.title, video.thumbnailUrl)
                        db.favoriteVideoDao().add(favoriteVideo)
                        mFavoredVideos.set(video.id, favoriteVideo)
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