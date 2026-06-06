package com.iiinaiii.punks.presentation.beers.model

import com.iiinaiii.punks.presentation.util.event.Event

data class HomeUiModel(
    val showProgress: Boolean,
    val showSuccess: Event<BeerResultUiModel>?,
    val showError: Event<Unit>?
)
