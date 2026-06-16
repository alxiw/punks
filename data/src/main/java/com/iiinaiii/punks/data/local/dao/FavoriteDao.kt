package com.iiinaiii.punks.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iiinaiii.punks.data.local.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface FavoriteDao {

    @Query("SELECT id FROM favorites")
    fun getFavoriteIdsFlow(): Flow<List<Int>>

    @Query("SELECT id FROM favorites")
    suspend fun getFavoriteIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteBeer: FavoriteEntity)

    @Delete
    suspend fun delete(favoriteBeer: FavoriteEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean
}
