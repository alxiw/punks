package com.iiinaiii.punks.domain

import androidx.paging.PagingData
import com.iiinaiii.punks.domain.model.Beer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteBeersFlowUseCase @Inject constructor(
    private val beersRepository: BeersRepository
) {
    operator fun invoke(): Flow<PagingData<Beer>> {
        return beersRepository.getFavoriteBeersFlow()
    }
}
