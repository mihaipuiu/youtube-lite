package com.mihaipuiu.youtubelite.models

import androidx.room.*

@Dao
interface FavoriteVideoDao {
    @Query("SELECT * FROM favorites")
    suspend fun getAll(): List<FavoriteVideo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(video: FavoriteVideo)

    @Query("SELECT * FROM favorites WHERE id = :id")
    suspend fun getById(id: String): FavoriteVideo

    @Delete
    suspend fun remove(video: FavoriteVideo)
}