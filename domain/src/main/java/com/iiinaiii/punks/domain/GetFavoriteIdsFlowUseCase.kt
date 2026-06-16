package com.iiinaiii.punks.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteIdsFlowUseCase @Inject constructor(
    private val beersRepository: BeersRepository
) {
    operator fun invoke(): Flow<Set<Int>> {
        return beersRepository.getFavoriteIdsFlow()
    }
}
