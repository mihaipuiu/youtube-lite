package com.mihaipuiu.youtubelite.services

import android.app.DownloadManager
import com.mihaipuiu.youtubelite.models.Video
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class YoutubeService {
    private val apiKey = "AIzaSyDMhbSDf6YR1Qt7_Jlwk5zzhfUxPLoedIk"
    private val mostPopularVideosUrl = "https://www.googleapis.com/youtube/v3/videos?part=snippet&chart=mostPopular&maxResults=50&regionCode=US&key=$apiKey"
    private val searchVideosUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&key=$apiKey"

    fun getMostPopularVideos(): ArrayList<Video> {
        val videos = ArrayList<Video>()
        val jsonResult = JSONObject(URL(this.mostPopularVideosUrl).readText())
        val items = JSONArray(jsonResult.get("items").toString())

        for (i in 0..items.length()) {
            try {
                val item = items[i] as JSONObject
                val snippet = item.getJSONObject("snippet")
                val thumbnails = snippet.getJSONObject("thumbnails")
                val default = thumbnails.getJSONObject("default")

                val id = item.getString("id") as String
                val title = snippet.getString("title") as String
                val url = default.getString("url") as String

                if (id.isNotEmpty() && title.isNotEmpty() && url.isNotEmpty()) {
                    videos.add(Video(id, title, url))
                }
            } catch (e: Exception) {

            }
        }

        return videos
    }

    fun getSearchVideos(query: String): ArrayList<Video> {
        val videos = ArrayList<Video>()
        val jsonResult = JSONObject(URL(this.searchVideosUrl + "&q=" + query).readText())
        val items = JSONArray(jsonResult.get("items").toString())

        for (i in 0..items.length()) {
            try {
                val item = items[i] as JSONObject
                val id = item.getJSONObject("id")
                val snippet = item.getJSONObject("snippet")
                val thumbnails = snippet.getJSONObject("thumbnails")
                val default = thumbnails.getJSONObject("default")

                val videoId = id.getString("videoId") as String
                val title = snippet.getString("title") as String
                val url = default.getString("url") as String

                if (videoId.isNotEmpty() && title.isNotEmpty() && url.isNotEmpty()) {
                    videos.add(Video(videoId, title, url))
                }
            } catch (e: Exception) {

            }
        }

        return videos
    }
}