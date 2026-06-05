package com.iiinaiii.punks.ui.beers

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iiinaiii.punks.R
import com.iiinaiii.punks.databinding.ActivityBeersBinding
import com.iiinaiii.punks.ui.util.delegates.contentView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeersActivity : AppCompatActivity() {

    private val viewModel: BeersViewModel by viewModels()
    lateinit var beersAdapter: BeersAdapter

    private val binding by contentView<BeersActivity, ActivityBeersBinding>(
        R.layout.activity_beers
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupRecyclerView()
        viewModel.uiModel.observe(this, Observer {
            val uiModel = it ?: return@Observer

            if (uiModel.showSuccess != null && !uiModel.showSuccess.consumed) {
                uiModel.showSuccess.consume()?.let { beerResult ->
                    updateBeers(beerResult.beers)
                }
            }

            if (uiModel.showError != null && !uiModel.showError.consumed) {
                uiModel.showError.consume()?.let {
                    showError()
                }
            }
        })
        viewModel.loadBeers()
    }

    private fun setupRecyclerView() {
        beersAdapter = BeersAdapter(this)
        binding.homeRecyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(this@BeersActivity)
            layoutManager = linearLayoutManager
            adapter = beersAdapter
            addOnScrollListener(object : InfiniteScrollListener(linearLayoutManager, viewModel) {
                override fun onLoadMore() {
                    viewModel.loadBeers()
                }
            })
        }
    }

    private fun updateBeers(beers: List<BeerItemUiModel>) {
        beersAdapter.also {
            it.beers.addAll(beers)
            it.notifyDataSetChanged()
        }
    }

    private fun showError() {
        Snackbar.make(binding.root, R.string.beer_search_error, Snackbar.LENGTH_SHORT).show()
    }
}
