package com.iiinaiii.punks.presentation.beers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.iiinaiii.punks.domain.GetBeersFlowUseCase
import com.iiinaiii.punks.presentation.beers.model.BeerItemUiModel
import com.iiinaiii.punks.presentation.beers.model.toItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class BeersViewModel @Inject constructor(
    getBeersFlowUseCase: GetBeersFlowUseCase,
) : ViewModel() {

    internal val beersFlow: Flow<PagingData<BeerItemUiModel>> = getBeersFlowUseCase()
        .map { pagingData ->
            pagingData.map { beer -> beer.toItemUiModel() }
        }
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)
}
