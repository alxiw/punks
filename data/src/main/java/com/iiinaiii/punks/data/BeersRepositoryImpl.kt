package com.iiinaiii.punks.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.iiinaiii.punks.data.remote.BeerPagingSource
import com.iiinaiii.punks.data.remote.BeersRemoteDataSource
import com.iiinaiii.punks.data.remote.api.model.toBeer
import com.iiinaiii.punks.domain.BeersRepository
import com.iiinaiii.punks.domain.model.Beer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BeersRepositoryImpl @Inject constructor(
    private val remoteDataSource: BeersRemoteDataSource
) : BeersRepository {

    override fun getBeersFlow(): Flow<PagingData<Beer>> = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { BeerPagingSource(remoteDataSource) }
    ).flow

    override suspend fun getBeer(id: Int): Result<Beer> = remoteDataSource.getBeer(id).map { it.toBeer() }
}
