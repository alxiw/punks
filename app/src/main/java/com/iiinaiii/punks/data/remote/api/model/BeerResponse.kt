package com.iiinaiii.punks.data.remote.api.model

import com.iiinaiii.punks.domain.model.Beer
import com.iiinaiii.punks.util.ValueUnit
import org.threeten.bp.Year
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import org.threeten.bp.format.TextStyle
import java.util.Locale

data class BeerResponse(
    val id: Int,
    val name: String,
    val tagline: String,
    val first_brewed: String,
    val description: String,
    val image: String,
    val abv: Float?,
    val ibu: Float?,
    val target_fg: Float?,
    val target_og: Float?,
    val ebc: Float?,
    val srm: Float?,
    val volume: VolumeResponse,
    val boil_volume: VolumeResponse,
    val ph: Float?,
    val attenuation_level: Float?,
    val food_pairing: List<String>,
    val brewers_tips: String
)

fun BeerResponse.toBeer() = Beer(
    id = id,
    name = name,
    tagline = tagline,
    firstBrewed = first_brewed.toDateOrDefault(),
    description = description,
    imageUrl = "https://punkapi-alxiw.amvera.io/v3/images/$image",
    abv = "${abv.toStringOrDefault()}%",
    ibu = ibu.toStringOrDefault(),
    targetOg = target_og.toStringOrDefault(),
    targetFg = target_fg.toStringOrDefault(),
    ebc = ebc.toStringOrDefault(),
    srm = srm.toStringOrDefault(),
    volume = volume.toVolumeText(),
    boilVolume = boil_volume.toVolumeText(),
    ph = ph.toStringOrDefault(),
    attenuationLevel = attenuation_level.toStringOrDefault(),
    foodPairing = food_pairing,
    brewersTips = brewers_tips
)

data class VolumeResponse(
    val value: Int,
    val unit: String
)

fun VolumeResponse.toVolumeText(): String {
    return value.toString() + ValueUnit.convertToDisplayName(unit)
}

private fun Float?.toStringOrDefault(): String {
    return this?.toCleanString() ?: EMPTY_PLACEHOLDER
}

private fun String?.toDateOrDefault(): String {
    return this?.let { formatDate(it) } ?: EMPTY_PLACEHOLDER
}

fun Float.toCleanString(): String {
    return if (this % 1.0 == 0.0) toLong().toString() else toString()
}

private fun formatDate(string: String?): String {
    if (string == null) {
        return EMPTY_PLACEHOLDER
    }
    return try {
        YearMonth.parse(string, MONTH_YEAR_FORMATTER).run {
            "${month.getDisplayName(TextStyle.FULL, Locale.US).uppercase(Locale.US)} $year"
        }
    } catch (e: DateTimeParseException) {
        try {
            Year.parse(string, YEAR_FORMATTER).run {
                toString()
            }
        } catch (e: DateTimeParseException) {
            string
        }
    }
}

private const val EMPTY_PLACEHOLDER = "∅"

private val MONTH_YEAR_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy")
private val YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy")
