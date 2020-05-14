package com.mihaipuiu.youtubelite.services

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mihaipuiu.youtubelite.models.FavoriteVideo
import com.mihaipuiu.youtubelite.models.FavoriteVideoDao

@Database(entities = arrayOf(FavoriteVideo::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): FavoriteVideoDao
}