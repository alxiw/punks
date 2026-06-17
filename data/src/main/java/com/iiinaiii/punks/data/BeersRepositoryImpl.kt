package com.iiinaiii.punks.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.iiinaiii.punks.data.local.BeersLocalDataSource
import com.iiinaiii.punks.data.remote.BeerPagingSource
import com.iiinaiii.punks.data.remote.BeersRemoteDataSource
import com.iiinaiii.punks.data.remote.FavoritePagingSource
import com.iiinaiii.punks.data.remote.model.toBeer
import com.iiinaiii.punks.domain.BeersRepository
import com.iiinaiii.punks.domain.model.Beer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BeersRepositoryImpl @Inject constructor(
    private val remoteDataSource: BeersRemoteDataSource,
    private val localDataSource: BeersLocalDataSource
) : BeersRepository {

    override fun getBeersFlow(): Flow<PagingData<Beer>> = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { BeerPagingSource(remoteDataSource, localDataSource) }
    ).flow

    override fun getFavoriteBeersFlow(): Flow<PagingData<Beer>> = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { FavoritePagingSource(remoteDataSource, localDataSource) }
    ).flow

    override fun getFavoriteIdsFlow(): Flow<Set<Int>> = localDataSource.getFavoriteIdsFlow()
        .map { it.toSet() }

    override suspend fun getBeer(id: Int): Result<Beer> {
        return remoteDataSource.getBeer(id).map { it.toBeer().copy(isFavorite = localDataSource.isFavorite(id)) }
    }

    override suspend fun toggleFavorite(id: Int) {
        if (localDataSource.isFavorite(id)) {
            localDataSource.removeFavorite(id)
        } else {
            localDataSource.addFavorite(id)
        }
    }
}
