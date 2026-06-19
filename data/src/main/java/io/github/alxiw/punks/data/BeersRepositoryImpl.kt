package io.github.alxiw.punks.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.alxiw.punks.data.local.BeersLocalDataSource
import io.github.alxiw.punks.data.remote.BeerPagingSource
import io.github.alxiw.punks.data.remote.BeersRemoteDataSource
import io.github.alxiw.punks.data.remote.FavoritePagingSource
import io.github.alxiw.punks.data.remote.model.toBeer
import io.github.alxiw.punks.domain.BeersRepository
import io.github.alxiw.punks.domain.model.Beer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BeersRepositoryImpl @Inject constructor(
    private val remoteDataSource: BeersRemoteDataSource,
    private val localDataSource: BeersLocalDataSource
) : BeersRepository {

    override fun getBeersFlow(query: String?): Flow<PagingData<Beer>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { BeerPagingSource(query, remoteDataSource, localDataSource) }
    ).flow

    override fun getFavoriteBeersFlow(): Flow<PagingData<Beer>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
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

    companion object {
        private const val PAGE_SIZE = 30
    }
}
