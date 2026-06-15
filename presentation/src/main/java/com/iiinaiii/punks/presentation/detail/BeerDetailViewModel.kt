package com.iiinaiii.punks.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiinaiii.punks.domain.GetBeerUseCase
import com.iiinaiii.punks.presentation.detail.model.BeerDetailUiState
import com.iiinaiii.punks.presentation.detail.model.toDetailUiModel
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
    internal val uiState: StateFlow<BeerDetailUiState> = _uiState.asStateFlow()

    init {
        val beerId: Int = requireNotNull(savedStateHandle["beerId"]) {
            "BeerDetailViewModel requires beerId in SavedStateHandle"
        }
        loadBeer(beerId)
    }

    private fun loadBeer(beerId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = getBeer(beerId).fold(
                onSuccess = { BeerDetailUiState.Success(it.toDetailUiModel()) },
                onFailure = { BeerDetailUiState.Error(it) }
            )
        }
    }
}
