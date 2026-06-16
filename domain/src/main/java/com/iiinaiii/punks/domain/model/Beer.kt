package com.iiinaiii.punks.domain.model

data class Beer(
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
    val brewersTips: String,
    val contributedBy: String,
    val isFavorite: Boolean = false
)
