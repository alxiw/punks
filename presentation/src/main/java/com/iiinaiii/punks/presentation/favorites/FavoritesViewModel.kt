package com.iiinaiii.punks.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.iiinaiii.punks.domain.GetFavoriteBeersFlowUseCase
import com.iiinaiii.punks.domain.GetFavoriteIdsFlowUseCase
import com.iiinaiii.punks.domain.ToggleFavoriteUseCase
import com.iiinaiii.punks.presentation.beers.model.BeerItemUiModel
import com.iiinaiii.punks.presentation.beers.model.toItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteBeersFlowUseCase: GetFavoriteBeersFlowUseCase,
    getFavoriteIdsFlowUseCase: GetFavoriteIdsFlowUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    internal val favoritesFlow: Flow<PagingData<BeerItemUiModel>> = getFavoriteBeersFlowUseCase()
        .map { pagingData ->
            pagingData.map { beer -> beer.toItemUiModel() }
        }
        .cachedIn(viewModelScope)

    internal val favoriteIds: StateFlow<Set<Int>> = getFavoriteIdsFlowUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    fun onFavoriteClick(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            toggleFavoriteUseCase(id)
        }
    }
}
