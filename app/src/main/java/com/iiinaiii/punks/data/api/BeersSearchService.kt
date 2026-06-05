package com.iiinaiii.punks.data.api

import android.net.Uri
import com.iiinaiii.punks.data.beers.model.BeerResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BeersSearchService {

    @GET("beers")
    fun search(
        @Query("page") page: Int
    ): Deferred<Response<List<BeerResponse>>>

    @GET("beers/{id}")
    fun getBeer(
        @Path("id") id: Int
    ): Deferred<Response<BeerResponse>>

    companion object {
        val ENDPOINT = Uri.Builder().scheme("https")
            .authority("punkapi-alxiw.amvera.io")
            .appendEncodedPath("v3/").build().toString()
    }
}