package com.iiinaiii.punks.data.remote.api.model

import com.iiinaiii.punks.data.remote.api.util.toDateOrDefault
import com.iiinaiii.punks.data.remote.api.util.toStringOrDefault
import com.iiinaiii.punks.data.remote.api.util.toVolumeText
import com.iiinaiii.punks.domain.model.Beer
import com.squareup.moshi.Json

data class BeerResponse(
    val id: Int,
    val name: String,
    val tagline: String,
    @Json(name = "first_brewed") val firstBrewed: String,
    val description: String,
    val image: String,
    val abv: Float?,
    val ibu: Float?,
    @Json(name = "target_fg") val targetFg: Float?,
    @Json(name = "target_og") val targetOg: Float?,
    val ebc: Float?,
    val srm: Float?,
    val volume: VolumeResponse,
    @Json(name = "boil_volume") val boilVolume: VolumeResponse,
    val ph: Float?,
    @Json(name = "attenuation_level") val attenuationLevel: Float?,
    @Json(name = "food_pairing") val foodPairing: List<String>,
    @Json(name = "brewers_tips") val brewersTips: String
)

data class VolumeResponse(
    val value: Int,
    val unit: String
)

fun BeerResponse.toBeer() = Beer(
    id = id,
    name = name,
    tagline = tagline,
    firstBrewed = firstBrewed.toDateOrDefault(),
    description = description,
    imageUrl = "https://punkapi-alxiw.amvera.io/v3/images/$image",
    abv = "${abv.toStringOrDefault()}%",
    ibu = ibu.toStringOrDefault(),
    targetOg = targetOg.toStringOrDefault(),
    targetFg = targetFg.toStringOrDefault(),
    ebc = ebc.toStringOrDefault(),
    srm = srm.toStringOrDefault(),
    volume = volume.toVolumeText(),
    boilVolume = boilVolume.toVolumeText(),
    ph = ph.toStringOrDefault(),
    attenuationLevel = attenuationLevel.toStringOrDefault(),
    foodPairing = foodPairing,
    brewersTips = brewersTips
)
