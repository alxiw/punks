package com.iiinaiii.punks.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.iiinaiii.punks.domain.model.Beer
import com.iiinaiii.punks.presentation.R
import com.iiinaiii.punks.presentation.databinding.ActivityBeerDetailBinding
import com.iiinaiii.punks.presentation.detail.item.HeaderItem
import com.iiinaiii.punks.presentation.detail.item.SimpleTextItem
import com.iiinaiii.punks.presentation.detail.item.ValueItem
import com.iiinaiii.punks.presentation.detail.model.BeerDetailUiState
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class BeerDetailActivity : AppCompatActivity() {

    private val viewModel: BeerDetailViewModel by viewModels()

    private val binding by viewBinding(ActivityBeerDetailBinding::bind)

    private val groupAdapter = GroupAdapter<GroupieViewHolder<ViewBinding>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_detail)
        setupRecyclerView()
        observeUiState()
    }

    private fun observeUiState() {
        viewModel.uiState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach(::render)
            .launchIn(lifecycleScope)
    }

    private fun render(state: BeerDetailUiState) {
        when (state) {
            is BeerDetailUiState.Loading -> showLoading()
            is BeerDetailUiState.Success -> showContent(state.model.beer)
            is BeerDetailUiState.Error -> showError(state.exception)
        }
    }

    private fun showLoading() {
        // do nothing
    }

    private fun showError(e: Throwable) {
        Snackbar.make(binding.root, R.string.beer_detail_error, Snackbar.LENGTH_SHORT).show()
    }

    private fun showContent(beer: Beer) {
        with(binding) {
            beerName.text = beer.name
            tagline.text = beer.tagline
            firstBrew.text = beer.firstBrewed
            topAbvValue.text = beer.abv
            topIbuValue.text = beer.ibu
            topOgValue.text = beer.targetOg
            beerPackage.load(beer.imageUrl)
        }
        updateBeerRecyclerData(beer)
    }

    private fun setupRecyclerView() {
        binding.detailRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@BeerDetailActivity)
            isNestedScrollingEnabled = false
            adapter = groupAdapter
        }
    }

    private fun updateBeerRecyclerData(beer: Beer) {
        // Description
        val descriptionSection = Section().apply {
            setHeader(HeaderItem(getString(R.string.header_description)))
            add(SimpleTextItem(beer.description))
        }

        // Basics
        val basicsSection = Section().apply {
            setHeader(HeaderItem(getString(R.string.header_basics)))
            add(ValueItem(getString(R.string.volume), beer.volume))
            add(ValueItem(getString(R.string.boil_volume), beer.boilVolume))
            add(ValueItem(getString(R.string.abv), beer.abv))
            add(ValueItem(getString(R.string.target_fg), beer.targetFg))
            add(ValueItem(getString(R.string.target_og), beer.targetOg))
            add(ValueItem(getString(R.string.ebc), beer.ebc))
            add(ValueItem(getString(R.string.srm), beer.srm))
            add(ValueItem(getString(R.string.ph), beer.ph))
            add(ValueItem(getString(R.string.attenuation_level), beer.attenuationLevel))
        }

        // Food pairing
        val foodPairingSection = Section().apply {
            setHeader(HeaderItem(getString(R.string.header_food_pairing)))
            addAll(
                beer.foodPairing.map { SimpleTextItem(it) }
            )
        }

        // Brewer's tips
        val tipsSection = Section().apply {
            setHeader(HeaderItem(getString(R.string.header_brewers_tips)))
            add(SimpleTextItem(beer.brewersTips))
        }

        groupAdapter.apply {
            clear()
            add(descriptionSection)
            add(basicsSection)
            add(foodPairingSection)
            add(tipsSection)
        }
    }

    companion object {
        private const val EXTRA_BEER_ID = "extra_beer_id"

        fun createIntent(context: Context, beerId: Int): Intent {
            return Intent(context, BeerDetailActivity::class.java)
                .putExtra(EXTRA_BEER_ID, beerId)
        }
    }
}
