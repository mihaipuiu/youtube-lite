package com.mihaipuiu.youtubelite.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.mihaipuiu.youtubelite.database.FavoriteVideosDb
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteVideosAdapter(private val mVideos: ArrayList<FavoriteVideo>, private val db: FavoriteVideosDb) :
    RecyclerView.Adapter<FavoriteVideosAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val videoThumbnailImageButton = itemView.findViewById<ImageView>(R.id.video_thumbnail)
        val videoTitleTextView = itemView.findViewById<TextView>(R.id.video_title_text)
        val videoPlayButton = itemView.findViewById<ImageButton>(R.id.play_video_button)
        val removeFromFavoritesButton = itemView.findViewById<ImageButton>(R.id.add_favorite_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteVideosAdapter.ViewHolder {
        this.context = parent.context
        val inflater = LayoutInflater.from(this.context)
        val contactView = inflater.inflate(R.layout.item_video, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(viewHolder: FavoriteVideosAdapter.ViewHolder, position: Int) {
        val video: FavoriteVideo = mVideos.get(position)

        val imageView = viewHolder.videoThumbnailImageButton
        if (video.thumbnailUrl.isNotEmpty()) {
            Picasso.get().load(video.thumbnailUrl).into(imageView)
        }

        val textView = viewHolder.videoTitleTextView
        textView.text = video.title

        val playButton = viewHolder.videoPlayButton

        playButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.youtube.com/watch?v=" + video.id)
            this.context.startActivity(intent)
        })

        val addFavoriteButton = viewHolder.removeFromFavoritesButton
        addFavoriteButton.setOnClickListener(View.OnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                val task = async(Dispatchers.IO) {
                    val favoriteVideo = FavoriteVideo(video.id, video.title, video.thumbnailUrl)
                    db.favoriteVideoDao().remove(favoriteVideo)
                    mVideos.removeAt(position)
                }

                task.await()

                Toast.makeText(it.context, "Removed from favorites: " + video.title, Toast.LENGTH_SHORT).show()
                addFavoriteButton.setImageResource(android.R.drawable.star_big_off)
                addFavoriteButton.tag = null

                notifyItemRemoved(position)
            }
        })
    }

    override fun getItemCount(): Int {
        return mVideos.size
    }
}