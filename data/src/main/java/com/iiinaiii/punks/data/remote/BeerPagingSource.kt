package com.iiinaiii.punks.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.iiinaiii.punks.data.remote.api.model.toBeer
import com.iiinaiii.punks.domain.model.Beer

internal class BeerPagingSource(
    private val remoteDataSource: BeersRemoteDataSource
) : PagingSource<Int, Beer>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Beer> {
        val page = params.key ?: 1

        return remoteDataSource.getBeers(page).fold(
            onSuccess = { data ->
                val beers = data.map { it.toBeer() }
                LoadResult.Page(
                    data = beers,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (beers.isEmpty()) null else page + 1
                )
            },
            onFailure = {
                LoadResult.Error(it)
            }
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Beer>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
