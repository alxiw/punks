package com.iiinaiii.punks.domain

import com.iiinaiii.punks.domain.model.Beer
import com.iiinaiii.punks.domain.model.Result
import javax.inject.Inject

class SearchBeersUseCaseImpl @Inject constructor(
    private val beersRepository: BeersRepository
): SearchBeersUseCase {

    override suspend operator fun invoke(page: Int): Result<List<Beer>> {
        return beersRepository.search(page)
    }
}
