package com.iiinaiii.punks.data

import com.iiinaiii.punks.data.model.Result
import com.iiinaiii.punks.data.remote.BeersRemoteDataSource
import com.iiinaiii.punks.data.remote.api.model.BeerResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeersRepository @Inject constructor(private val remoteDataSource: BeersRemoteDataSource) {

    private val beerCache = mutableMapOf<Int, BeerResponse>()

    suspend fun search(
        page: Int
    ): Result<List<BeerResponse>> {
        val result = remoteDataSource.search(page)
        if (result is Result.Success) {
            cache(result.data)
        }
        return result
    }

    suspend fun getBeer(
        id: Int
    ): Result<BeerResponse> {
        val beer = beerCache[id]
        return if (beer != null) {
            Result.Success(beer)
        } else {
            Result.Error(IllegalStateException("Beer $id not cached"))
        }
    }

    private fun cache(data: List<BeerResponse>) {
        data.associateByTo(beerCache) { it.id }
    }
}
