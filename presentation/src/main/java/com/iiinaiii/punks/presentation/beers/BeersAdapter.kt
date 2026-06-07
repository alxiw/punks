package com.iiinaiii.punks.presentation.beers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iiinaiii.punks.presentation.databinding.ItemBeerBinding
import com.iiinaiii.punks.presentation.databinding.ItemLoadingBinding

class BeersAdapter(
    private val onItemClick: (BeerViewHolder.TransitionData) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val beers: MutableList<BeerItemUiModel> = mutableListOf()

    companion object {
        private const val TYPE_BEER = 0
        private const val TYPE_LOADING = 1
    }

    var showLoading: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                if (value) {
                    notifyItemInserted(beers.size)
                } else {
                    notifyItemRemoved(beers.size)
                }
            }
        }

    override fun getItemViewType(position: Int): Int {
        return if (position < beers.size) TYPE_BEER else TYPE_LOADING
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_BEER) {
            val binding = ItemBeerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            BeerViewHolder(binding) { data -> onItemClick(data) }
        } else {
            val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BeerViewHolder) {
            holder.bind(beers[position])
        }
    }

    override fun getItemCount(): Int = beers.size + if (showLoading) 1 else 0

    fun addBeers(newBeers: List<BeerItemUiModel>) {
        val startPosition = beers.size
        beers.addAll(newBeers)
        notifyItemRangeInserted(startPosition, newBeers.size)
    }

    class LoadingViewHolder(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)
}
