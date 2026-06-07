package com.iiinaiii.punks.presentation.beers.model

import com.iiinaiii.punks.presentation.util.event.Event

data class UiState(
    val showProgress: Boolean = false,
    val showFooterProgress: Boolean = false,
    val showSuccess: Event<BeerResultUiModel>? = null,
    val showError: Event<Unit>? = null
)
