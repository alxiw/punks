package com.iiinaiii.punks.presentation.beers

import com.iiinaiii.punks.domain.model.Beer

data class BeerItemUiModel(
    val id: Int,
    val name: String,
    val firstBrewed: String,
    val imageUrl: String,
    val abv: String
)

fun List<Beer>.toItemUiModels() = map { it.toItemUiModel() }

fun Beer.toItemUiModel() = BeerItemUiModel(
    id = id,
    name = name,
    imageUrl = imageUrl,
    firstBrewed = firstBrewed,
    abv = abv
)
