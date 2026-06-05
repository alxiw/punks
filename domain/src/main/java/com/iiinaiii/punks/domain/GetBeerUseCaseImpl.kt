package com.iiinaiii.punks.domain

import com.iiinaiii.punks.domain.model.Beer
import com.iiinaiii.punks.domain.model.Result
import javax.inject.Inject

class GetBeerUseCaseImpl @Inject constructor(
    private val beersRepository: BeersRepository
): GetBeerUseCase {

    override suspend operator fun invoke(id: Int): Result<Beer> {
        return beersRepository.getBeer(id)
    }
}
