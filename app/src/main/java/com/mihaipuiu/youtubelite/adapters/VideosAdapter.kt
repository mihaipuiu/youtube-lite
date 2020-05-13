package com.mihaipuiu.youtubelite.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mihaipuiu.youtubelite.R
import com.mihaipuiu.youtubelite.models.Video
import com.squareup.picasso.Picasso

class VideosAdapter(private val mVideos: List<Video>) :
    RecyclerView.Adapter<VideosAdapter.ViewHolder>() {
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val videoThumbnailImageButton = itemView.findViewById<ImageView>(R.id.video_thumbnail)
        val videoTitleTextView = itemView.findViewById<TextView>(R.id.video_title_text)
        val videoPlayButton = itemView.findViewById<ImageButton>(R.id.play_video_button)
        val addToFavoritesButton = itemView.findViewById<ImageButton>(R.id.add_favorite_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
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
        textView.setText(video.title)

        val playButton = viewHolder.videoPlayButton

        val addFavoriteButton = viewHolder.addToFavoritesButton
        addFavoriteButton.setOnClickListener(View.OnClickListener {

            if (addFavoriteButton.getTag() == 1) {
                // means that video is already added to favorites
                Toast.makeText(it.context, "Removed from favorites: " + video.title, Toast.LENGTH_SHORT).show()
                addFavoriteButton.setImageResource(android.R.drawable.star_big_off)
                addFavoriteButton.setTag(null)
            } else if (addFavoriteButton.getTag() == null) {
                Toast.makeText(it.context, "Added to favorites: " + video.title, Toast.LENGTH_SHORT).show()
                addFavoriteButton.setImageResource(android.R.drawable.star_big_on)
                addFavoriteButton.setTag(1)
            }
        })
    }

    override fun getItemCount(): Int {
        return mVideos.size
    }
}