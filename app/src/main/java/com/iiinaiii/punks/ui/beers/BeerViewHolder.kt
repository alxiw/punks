package com.iiinaiii.punks.ui.beers

import android.util.Pair
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.iiinaiii.punks.R
import com.iiinaiii.punks.databinding.ItemBeerBinding
import coil.load

class BeerViewHolder(
    private val binding: ItemBeerBinding,
    private val onItemClicked: (data: TransitionData) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private var beer: BeerItemUiModel? = null

    init {
        binding.root.setOnClickListener {
            beer?.let { beer ->
                onItemClicked(
                    TransitionData(beer, getSharedElementsForTransition())
                )
            }
        }
    }

    fun bind(uiModel: BeerItemUiModel) {
        beer = uiModel

        binding.beerName.text = uiModel.name
        binding.firstBrew.text = uiModel.firstBrewed
        binding.beerAbv.text = uiModel.abv

        binding.beerImage.load(uiModel.imageUrl)
    }

    private fun getSharedElementsForTransition(): Array<Pair<View, String>> {
        val resources = binding.root.resources
        return arrayOf(
            Pair(binding.beerImage as View, resources.getString(R.string.transition_beer_image)),
            Pair(binding.beerName as View, resources.getString(R.string.transition_beer_name))
        )
    }

    data class TransitionData(
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
}