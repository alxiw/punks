package com.iiinaiii.punks.presentation.detail.model

import android.util.Pair
import android.view.View
import com.iiinaiii.punks.presentation.beers.model.BeerItemUiModel

internal data class TransitionData(
    val beer: BeerItemUiModel,
    val sharedElements: Array<Pair<View, String>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TransitionData

        if (beer != other.beer) return false
        if (!sharedElements.contentEquals(other.sharedElements)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = beer.hashCode()
        result = 31 * result + sharedElements.contentHashCode()
        return result
    }
}
