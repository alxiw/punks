package com.iiinaiii.punks.presentation.beers

import android.app.ActivityOptions
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iiinaiii.punks.presentation.R
import com.iiinaiii.punks.presentation.databinding.ActivityBeersBinding
import com.iiinaiii.punks.presentation.detail.BeerDetailActivity
import com.iiinaiii.punks.presentation.util.setVisibility
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BeersActivity : AppCompatActivity() {

    private val viewModel: BeersViewModel by viewModels()

    private val binding by viewBinding(ActivityBeersBinding::bind)

    private val beersAdapter: BeersAdapter = BeersAdapter(
        onItemClick = { data -> openBeerDetail(data) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beers)

        setupRecyclerView()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiModel.collect { uiModel ->
                    binding.loading.setVisibility(uiModel.showProgress)
                    beersAdapter.showLoading = uiModel.showFooterProgress

                    uiModel.showSuccess?.consume()?.let { beerResult ->
                        updateBeers(beerResult.beers)
                    }

                    uiModel.showError?.consume()?.let {
                        showError()
                    }
                }
            }
        }
        viewModel.loadBeers()
    }

    private fun setupRecyclerView() {
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
        beersAdapter.addBeers(beers)
    }

    private fun showError() {
        Snackbar.make(binding.root, R.string.beer_search_error, Snackbar.LENGTH_SHORT).show()
    }

    private fun openBeerDetail(data: BeerViewHolder.TransitionData) {
        val activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, *data.sharedElements)
        startActivity(BeerDetailActivity.createIntent(this, data.beer.id), activityOptions.toBundle())
    }
}
