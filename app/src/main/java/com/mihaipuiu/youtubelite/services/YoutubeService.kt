package com.mihaipuiu.youtubelite.services

import org.json.JSONObject
import java.net.URL

class YoutubeService {
    private val apiKey = "AIzaSyDMhbSDf6YR1Qt7_Jlwk5zzhfUxPLoedIk"
    private val mostPopularVideosUrl = "https://www.googleapis.com/youtube/v3/videos?part=snippet&chart=mostPopular&maxResults=50&regionCode=US&key=$apiKey"

    fun getMostPopularVideos(): JSONObject {
        val result = URL(this.mostPopularVideosUrl).readText()
        return JSONObject(result)
    }
}