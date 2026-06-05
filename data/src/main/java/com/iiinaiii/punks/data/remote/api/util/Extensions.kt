package com.iiinaiii.punks.data.remote.api.util

import com.iiinaiii.punks.data.remote.api.model.VolumeResponse

fun VolumeResponse.toVolumeText(): String {
    return value.toString() + ValueUnit.convertToDisplayName(unit)
}

fun Float?.toStringOrDefault(): String {
    return this?.toCleanString() ?: EMPTY_PLACEHOLDER
}

fun String?.toDateOrDefault(): String {
    return this?.let { LocalDateTimeAdapter.formatDate(it) } ?: EMPTY_PLACEHOLDER
}

fun Float.toCleanString(): String {
    return if (this % 1.0 == 0.0) toLong().toString() else toString()
}

private const val EMPTY_PLACEHOLDER = "∅"
