package com.mihaipuiu.youtubelite.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mihaipuiu.youtubelite.models.FavoriteVideo
import com.mihaipuiu.youtubelite.models.FavoriteVideoDao

@Database(entities = arrayOf(FavoriteVideo::class), version = 1)
abstract class FavoriteVideosDb : RoomDatabase() {
    abstract fun favoriteVideoDao(): FavoriteVideoDao

    companion object : SingletonHolder<FavoriteVideosDb, Context>({
        Room.databaseBuilder(it.applicationContext,
            FavoriteVideosDb::class.java, "youtube-lite")
            .build()
    })
}