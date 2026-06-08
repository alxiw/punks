package com.iiinaiii.punks.presentation.beers

import android.app.ActivityOptions
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iiinaiii.punks.presentation.R
import com.iiinaiii.punks.presentation.databinding.ActivityBeersBinding
import com.iiinaiii.punks.presentation.detail.BeerDetailActivity
import com.iiinaiii.punks.presentation.detail.model.TransitionData
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.flow.collectLatest
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
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@BeersActivity)
            adapter = beersAdapter.withLoadStateFooter(
                footer = BeersLoadStateAdapter { beersAdapter.retry() }
            )
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.beersFlow.collectLatest { pagingData ->
                        beersAdapter.submitData(pagingData)
                    }
                }

                launch {
                    beersAdapter.loadStateFlow.collectLatest { loadStates ->
                        binding.loading.isVisible = loadStates.refresh is LoadState.Loading

                        val errorState = loadStates.source.append as? LoadState.Error
                            ?: loadStates.source.prepend as? LoadState.Error
                            ?: loadStates.append as? LoadState.Error
                            ?: loadStates.prepend as? LoadState.Error
                            ?: loadStates.refresh as? LoadState.Error

                        errorState?.let {
                            showError(it.error)
                        }
                    }
                }
            }
        }
    }

    private fun showError(e: Throwable) {
        val string = resources.getString(R.string.beer_search_error, e.message ?: "\uD83E\uDD37")
        Snackbar.make(binding.root, string, Snackbar.LENGTH_SHORT).show()
    }

    private fun openBeerDetail(data: TransitionData) {
        val activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, *data.sharedElements)
        startActivity(BeerDetailActivity.createIntent(this, data.beer.id), activityOptions.toBundle())
    }
}
