package com.iiinaiii.punks.data.remote

import com.iiinaiii.punks.data.model.Result
import com.iiinaiii.punks.data.remote.api.BeersSearchService
import com.iiinaiii.punks.data.remote.api.model.BeerResponse
import com.iiinaiii.punks.util.safeApiCall
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeersRemoteDataSource @Inject constructor(
    private val service: BeersSearchService
) {

    suspend fun search(
        page: Int
    ): Result<List<BeerResponse>> = safeApiCall(
        call = { requestSearch(page) },
        errorMessage = "Error getting Breweries data"
    )

    private suspend fun requestSearch(
        page: Int
    ): Result<List<BeerResponse>> {
        val response = service.search(page).await()
        if (response.isSuccessful) {
            val body = response.body()
            if (!body.isNullOrEmpty()) {
                return Result.Success(body)
            }
        }
        return Result.Error(
            IOException("Error getting BeerResponse data ${response.code()} ${response.message()}")
        )
    }
}