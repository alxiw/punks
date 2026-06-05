package com.iiinaiii.punks.domain

import com.iiinaiii.punks.data.model.Result
import com.iiinaiii.punks.data.BeersRepository
import com.iiinaiii.punks.data.remote.api.model.toBeer
import com.iiinaiii.punks.domain.model.Beer
import com.iiinaiii.punks.util.exhaustive
import javax.inject.Inject

class SearchBeersUseCase @Inject constructor(
    private val beersRepository: BeersRepository
) {

    suspend operator fun invoke(page: Int): Result<List<Beer>> {
        val result = beersRepository.search(page)
        return when (result) {
            is Result.Success -> {
                Result.Success(result.data.map { it.toBeer() })
            }
            is Result.Error -> result
        }.exhaustive
    }

}