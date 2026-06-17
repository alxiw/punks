package com.iiinaiii.punks.data.remote.api

import android.net.Uri
import com.iiinaiii.punks.data.remote.model.BeerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface BeersSearchService {

    @GET("beers")
    suspend fun getBeers(
        @Query("page") page: Int
    ): Response<List<BeerResponse>>

    @GET("beers")
    suspend fun getBeersByIds(
        @Query("ids", encoded = true) ids: String,
        @Query("page") page: Int
    ): Response<List<BeerResponse>>

    @GET("beers/{id}")
    suspend fun getBeer(
        @Path("id") id: Int
    ): Response<BeerResponse>

    companion object {
        val ENDPOINT = Uri.Builder().scheme("https")
            .authority("punkapi-alxiw.amvera.io")
            .appendEncodedPath("v3/")
            .build()
            .toString()
    }
}
