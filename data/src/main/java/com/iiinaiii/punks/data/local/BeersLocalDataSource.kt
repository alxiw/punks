package com.iiinaiii.punks.data.local

import com.iiinaiii.punks.data.local.dao.FavoriteDao
import com.iiinaiii.punks.data.local.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BeersLocalDataSource @Inject constructor(
    private val favoriteDao: FavoriteDao
) {
    fun getFavoriteIdsFlow(): Flow<List<Int>> = favoriteDao.getFavoriteIdsFlow()

    suspend fun getFavoriteIds(): List<Int> = favoriteDao.getFavoriteIds()

    suspend fun isFavorite(id: Int): Boolean = favoriteDao.isFavorite(id)

    suspend fun addFavorite(id: Int) {
        favoriteDao.insert(FavoriteEntity(id))
    }

    suspend fun removeFavorite(id: Int) {
        favoriteDao.delete(FavoriteEntity(id))
    }
}
