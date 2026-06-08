package com.iiinaiii.punks.domain

import androidx.paging.PagingData
import com.iiinaiii.punks.domain.model.Beer
import kotlinx.coroutines.flow.Flow

interface BeersRepository {
    fun getBeersFlow(): Flow<PagingData<Beer>>
    suspend fun getBeer(id: Int): Result<Beer>
}
