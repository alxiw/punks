package io.github.alxiw.punks.presentation.detail.model

internal sealed interface BeerDetailUiState {
    data object Loading : BeerDetailUiState
    data class Success(val model: BeerDetailUiModel) : BeerDetailUiState
    data class Error(val exception: Throwable) : BeerDetailUiState
}
