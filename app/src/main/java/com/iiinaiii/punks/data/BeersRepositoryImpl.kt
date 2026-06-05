package com.iiinaiii.punks.data

import com.iiinaiii.punks.data.remote.BeersRemoteDataSource
import com.iiinaiii.punks.data.remote.api.model.BeerResponse
import com.iiinaiii.punks.data.remote.api.model.toBeer
import com.iiinaiii.punks.domain.BeersRepository
import com.iiinaiii.punks.domain.model.Beer
import com.iiinaiii.punks.domain.model.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeersRepositoryImpl @Inject constructor(
    private val remoteDataSource: BeersRemoteDataSource
) : BeersRepository {

    private val beerCache = mutableMapOf<Int, BeerResponse>()

    override suspend fun search(
        page: Int
    ): Result<List<Beer>> {
        val result = remoteDataSource.search(page)
        return when (result) {
            is Result.Success -> {
                cache(result.data)
                Result.Success(result.data.map { it.toBeer() })
            }
            is Result.Error -> result
        }
    }

    override suspend fun getBeer(
        id: Int
    ): Result<Beer> {
        val beer = beerCache[id]
        return if (beer != null) {
            Result.Success(beer.toBeer())
        } else {
            Result.Error(IllegalStateException("Beer $id not cached"))
        }
    }

    private fun cache(data: List<BeerResponse>) {
        data.associateByTo(beerCache) { it.id }
    }
}
