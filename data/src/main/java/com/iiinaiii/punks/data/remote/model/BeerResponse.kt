package com.iiinaiii.punks.data.remote.model

import com.iiinaiii.punks.data.remote.api.BeersSearchService
import com.iiinaiii.punks.data.remote.util.toDateOrDefault
import com.iiinaiii.punks.data.remote.util.toStringOrDefault
import com.iiinaiii.punks.data.remote.util.toVolumeText
import com.iiinaiii.punks.domain.model.Beer
import com.iiinaiii.punks.domain.model.Fermentation
import com.iiinaiii.punks.domain.model.Hop
import com.iiinaiii.punks.domain.model.Ingredients
import com.iiinaiii.punks.domain.model.Malt
import com.iiinaiii.punks.domain.model.MashTemp
import com.iiinaiii.punks.domain.model.Method
import com.squareup.moshi.Json

internal data class BeerResponse(
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
    val ph: Float?,
    @Json(name = "attenuation_level") val attenuationLevel: Float?,
    val volume: VolumeResponse,
    @Json(name = "boil_volume") val boilVolume: VolumeResponse,
    val method: MethodResponse,
    val ingredients: IngredientsResponse,
    @Json(name = "food_pairing") val foodPairing: List<String>,
    @Json(name = "brewers_tips") val brewersTips: String,
    @Json(name = "contributed_by") val contributedBy: String,
)

internal data class VolumeResponse(
    val value: Float?,
    val unit: String
)

internal data class MethodResponse(
    @Json(name = "mash_temp") val mashTemp: List<MashTempResponse>,
    val fermentation: FermentationResponse,
    val twist: String?
)

internal data class MashTempResponse(
    val temp: VolumeResponse,
    val duration: Int?
)

internal data class FermentationResponse(
    val temp: VolumeResponse
)

internal data class IngredientsResponse(
    val malt: List<MaltResponse>,
    val hops: List<HopResponse>,
    val yeast: String?
)

internal data class MaltResponse(
    val name: String,
    val amount: VolumeResponse
)

internal data class HopResponse(
    val name: String,
    val amount: VolumeResponse,
    val add: String,
    val attribute: String
)

internal fun BeerResponse.toBeer() = Beer(
    id = id,
    name = name,
    tagline = tagline,
    firstBrewed = firstBrewed.toDateOrDefault(),
    description = description,
    imageUrl = "${BeersSearchService.ENDPOINT}images/$image",
    abv = "${abv.toStringOrDefault()}%",
    ibu = ibu.toStringOrDefault(),
    targetOg = targetOg.toStringOrDefault(),
    targetFg = targetFg.toStringOrDefault(),
    ebc = ebc.toStringOrDefault(),
    srm = srm.toStringOrDefault(),
    ph = ph.toStringOrDefault(),
    attenuationLevel = attenuationLevel.toStringOrDefault(),
    volume = volume.toVolumeText(),
    boilVolume = boilVolume.toVolumeText(),
    method = method.toDomain(),
    ingredients = ingredients.toDomain(),
    foodPairing = foodPairing,
    brewersTips = brewersTips,
    contributedBy = contributedBy
)

private fun MethodResponse.toDomain() = Method(
    mashTemp = mashTemp.map { it.toDomain() },
    fermentation = fermentation.toDomain(),
    twist = twist
)

private fun MashTempResponse.toDomain() = MashTemp(
    temp = temp.toVolumeText(),
    duration = duration.toStringOrDefault()
)

private fun FermentationResponse.toDomain() = Fermentation(
    temp = temp.toVolumeText()
)

private fun IngredientsResponse.toDomain() = Ingredients(
    malt = malt.map { it.toDomain() },
    hops = hops.map { it.toDomain() },
    yeast = yeast.toStringOrDefault()
)

private fun MaltResponse.toDomain() = Malt(
    name = name,
    amount = amount.toVolumeText()
)

private fun HopResponse.toDomain() = Hop(
    name = name,
    amount = amount.toVolumeText(),
    add = add,
    attribute = attribute
)
