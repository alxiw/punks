package com.iiinaiii.punks.presentation.beers

import android.app.Activity
import android.app.ActivityOptions
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iiinaiii.punks.presentation.databinding.ItemBeerBinding
import com.iiinaiii.punks.presentation.databinding.ItemLoadingBinding
import com.iiinaiii.punks.presentation.detail.BeerDetailActivity

class BeersAdapter(
    private val host: Activity,
    val beers: MutableList<BeerItemUiModel> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            BeerViewHolder(binding) { data -> openBeerDetail(data) }
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

    private fun openBeerDetail(data: BeerViewHolder.TransitionData) {
        val activityOptions = ActivityOptions.makeSceneTransitionAnimation(host, *data.sharedElements)
        host.startActivity(BeerDetailActivity.createIntent(host, data.beer.id), activityOptions.toBundle())
    }

    class LoadingViewHolder(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)
}
