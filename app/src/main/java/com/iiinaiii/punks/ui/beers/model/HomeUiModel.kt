package com.iiinaiii.punks.ui.beers.model

import com.iiinaiii.punks.util.event.Event

data class HomeUiModel(
    val showProgress: Boolean,
    val showSuccess: Event<BeerResultUiModel>?,
    val showError: Event<Unit>?
)
