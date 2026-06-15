package com.iiinaiii.punks.presentation.beers.model

import com.iiinaiii.punks.domain.model.Beer

internal data class BeerItemUiModel(
    val id: Int,
    val name: String,
    val tagline: String,
    val firstBrewed: String,
    val imageUrl: String,
    val abv: String
)

internal fun Beer.toItemUiModel() = BeerItemUiModel(
    id = id,
    name = name,
    tagline = tagline,
    imageUrl = imageUrl,
    firstBrewed = firstBrewed,
    abv = abv
)
