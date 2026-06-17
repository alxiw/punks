package com.iiinaiii.punks.presentation.beers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.iiinaiii.punks.domain.GetBeersFlowUseCase
import com.iiinaiii.punks.domain.GetFavoriteIdsFlowUseCase
import com.iiinaiii.punks.domain.ToggleFavoriteUseCase
import com.iiinaiii.punks.presentation.beers.model.BeerItemUiModel
import com.iiinaiii.punks.presentation.beers.model.toItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeersViewModel @Inject constructor(
    getBeersFlowUseCase: GetBeersFlowUseCase,
    getFavoriteIdsFlowUseCase: GetFavoriteIdsFlowUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val initialQuery: String = savedStateHandle[LAST_SEARCH_QUERY_KEY] ?: DEFAULT_QUERY

    private val actionStateFlow = MutableSharedFlow<String>()

    private val searches = actionStateFlow
        .distinctUntilChanged()
        .onStart { emit(initialQuery) }

    private val state: StateFlow<String> = searches
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = DEFAULT_QUERY
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    internal val beersFlow: Flow<PagingData<BeerItemUiModel>> = searches
        .flatMapLatest { query -> getBeersFlowUseCase(query) }
        .map { pagingData -> pagingData.map { beer -> beer.toItemUiModel() } }
        .cachedIn(viewModelScope)

    internal val favoriteIds: StateFlow<Set<Int>> = getFavoriteIdsFlowUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    fun accept(query: String) {
        viewModelScope.launch { actionStateFlow.emit(query) }
    }

    fun onFavoriteClick(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            toggleFavoriteUseCase(id)
        }
    }

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY_KEY] = state.value
        super.onCleared()
    }

    companion object {
        private const val LAST_SEARCH_QUERY_KEY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""
    }
}
