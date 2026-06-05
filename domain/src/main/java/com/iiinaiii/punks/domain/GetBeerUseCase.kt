package com.iiinaiii.punks.domain

import com.iiinaiii.punks.domain.model.Beer
import com.iiinaiii.punks.domain.model.Result

interface GetBeerUseCase {
    suspend operator fun invoke(id: Int): Result<Beer>
}