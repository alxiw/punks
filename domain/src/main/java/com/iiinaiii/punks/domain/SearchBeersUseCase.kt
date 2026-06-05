package com.iiinaiii.punks.domain

import com.iiinaiii.punks.domain.model.Beer
import com.iiinaiii.punks.domain.model.Result

interface SearchBeersUseCase {
    suspend operator fun invoke(page: Int): Result<List<Beer>>
}