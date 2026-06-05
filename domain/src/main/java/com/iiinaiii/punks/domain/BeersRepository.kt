package com.iiinaiii.punks.domain

import com.iiinaiii.punks.domain.model.Beer
import com.iiinaiii.punks.domain.model.Result

interface BeersRepository {
    suspend fun search(page: Int): Result<List<Beer>>
    suspend fun getBeer(id: Int): Result<Beer>
}
