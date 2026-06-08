package com.iiinaiii.punks.presentation.beers

import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.iiinaiii.punks.presentation.R
import com.iiinaiii.punks.presentation.beers.model.BeerItemUiModel
import com.iiinaiii.punks.presentation.databinding.ItemBeerBinding
import com.iiinaiii.punks.presentation.detail.model.TransitionData
import com.iiinaiii.punks.presentation.util.loadImage

internal class BeersAdapter(
    private val onItemClick: (TransitionData) -> Unit
) : PagingDataAdapter<BeerItemUiModel, BeersAdapter.BeerViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val binding = ItemBeerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BeerViewHolder(binding) { data -> onItemClick(data) }
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class BeerViewHolder(
        private val binding: ItemBeerBinding,
        private val onItemClick: (data: TransitionData) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(uiModel: BeerItemUiModel) {
            with(binding) {
                beerName.text = uiModel.name
                firstBrew.text = uiModel.firstBrewed
                beerAbv.text = uiModel.abv
                beerId.text = String.format("#%s", uiModel.id)

                beerImage.loadImage(uiModel.imageUrl)

                root.setOnClickListener {
                    onItemClick(TransitionData(uiModel, getSharedElementsForTransition()))
                }
            }
        }

        private fun getSharedElementsForTransition(): Array<Pair<View, String>> {
            val resources = binding.root.resources
            return arrayOf(
                Pair(binding.beerImage as View, resources.getString(R.string.transition_beer_image)),
                Pair(binding.beerName as View, resources.getString(R.string.transition_beer_name)),
                Pair(binding.beerAbv as View, resources.getString(R.string.transition_beer_abv)),
                Pair(binding.firstBrew as View, resources.getString(R.string.transition_beer_date))
            )
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BeerItemUiModel>() {
            override fun areItemsTheSame(oldItem: BeerItemUiModel, newItem: BeerItemUiModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BeerItemUiModel, newItem: BeerItemUiModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
