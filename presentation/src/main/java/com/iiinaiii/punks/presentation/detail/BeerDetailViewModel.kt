package com.iiinaiii.punks.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiinaiii.punks.domain.GetBeerUseCase
import com.iiinaiii.punks.domain.model.Result
import com.iiinaiii.punks.presentation.detail.model.BeerDetailUiModel
import com.iiinaiii.punks.presentation.detail.model.BeerDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBeer: GetBeerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BeerDetailUiState>(BeerDetailUiState.Loading)
    val uiState: StateFlow<BeerDetailUiState> = _uiState.asStateFlow()

    private val beerId: Int = requireNotNull(savedStateHandle["extra_beer_id"]) {
        "BeerDetailViewModel requires extra_beer_id in SavedStateHandle"
    }

    init {
        loadBeer(beerId)
    }

    private fun loadBeer(beerId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.value = when (val result = getBeer(beerId)) {
                is Result.Success -> BeerDetailUiState.Success(BeerDetailUiModel(result.data))
                is Result.Error -> BeerDetailUiState.Error(result.exception)
            }
        }
    }
}
