package com.iiinaiii.punks.presentation.detail.model

import com.iiinaiii.punks.domain.model.Beer

internal data class BeerDetailUiModel(
    val id: Int,
    val name: String,
    val tagline: String,
    val firstBrewed: String,
    val description: String,
    val imageUrl: String,
    val abv: String,
    val ibu: String,
    val targetOg: String,
    val targetFg: String,
    val ebc: String,
    val srm: String,
    val volume: String,
    val boilVolume: String,
    val ph: String,
    val attenuationLevel: String,
    val foodPairing: List<String>,
    val brewersTips: String
)

internal fun Beer.toDetailUiModel() = BeerDetailUiModel(
    id = id,
    name = name,
    tagline = tagline,
    firstBrewed = firstBrewed,
    description = description,
    imageUrl = imageUrl,
    abv = abv,
    ibu = ibu,
    targetOg = targetOg,
    targetFg = targetFg,
    ebc = ebc,
    srm = srm,
    volume = volume,
    boilVolume = boilVolume,
    ph = ph,
    attenuationLevel = attenuationLevel,
    foodPairing = foodPairing,
    brewersTips = brewersTips
)
