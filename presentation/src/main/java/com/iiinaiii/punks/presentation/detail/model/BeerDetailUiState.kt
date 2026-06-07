package com.iiinaiii.punks.presentation.detail.model

sealed interface BeerDetailUiState {
    data object Loading : BeerDetailUiState
    data class Success(val model: BeerDetailUiModel) : BeerDetailUiState
    data class Error(val exception: Throwable) : BeerDetailUiState
}
