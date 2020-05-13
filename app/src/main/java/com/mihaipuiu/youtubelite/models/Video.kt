package com.mihaipuiu.youtubelite.models

class Video(val id: String, val title: String, val thumbnailUrl: String) {
    companion object {
        fun createEmptyList(): ArrayList<Video> {
            return ArrayList<Video>()
        }
    }
}