package com.iiinaiii.punks.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.iiinaiii.punks.presentation.detail.model.BeerDetailUiModel
import com.iiinaiii.punks.presentation.detail.model.BeerDetailUiState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BeerDetailScreen(
    viewModel: BeerDetailViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is BeerDetailUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is BeerDetailUiState.Success -> {
                BeerDetailContent(
                    beer = state.model,
                    animatedVisibilityScope = animatedVisibilityScope,
                    sharedTransitionScope = sharedTransitionScope,
                    onBackClick = onBackClick
                )
            }
            is BeerDetailUiState.Error -> {
                Text(
                    text = state.exception.message ?: "Error",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun BeerDetailContent(
    beer: BeerDetailUiModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onBackClick: () -> Unit
) {
    with(sharedTransitionScope) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = beer.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(300.dp)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "image-${beer.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                    contentScale = ContentScale.Fit
                )
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = beer.name,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.sharedElement(
                        sharedContentState = rememberSharedContentState(key = "name-${beer.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                )
                Text(
                    text = beer.tagline,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = beer.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )

                DetailSection("Basics") {
                    DetailRow("ABV", beer.abv)
                    DetailRow("IBU", beer.ibu)
                    DetailRow("EBC", beer.ebc)
                }
            }
        }
    }
}

@Composable
private fun DetailSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        content()
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(top = 8.dp)) {
        Text(text = "$label: ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun BeerDetailContentPreview() {
    val sampleBeer = BeerDetailUiModel(
        id = 1,
        name = "Punk IPA 2007 - 2010",
        tagline = "Post Modern Classic. Pale Ale.",
        firstBrewed = "APRIL 2007",
        description = "Our flagship beer that kick started the craft beer revolution. This is the beer that started it all - and it’s still as relevant today as it was back in 2007.",
        imageUrl = "https://punkapi-alxiw.amvera.io/v3/images/001.png",
        abv = "6",
        ibu = "60",
        targetOg = "1056",
        targetFg = "1010",
        ebc = "17",
        srm = "8.5",
        volume = "20L",
        boilVolume = "25L",
        ph = "4.4",
        attenuationLevel = "82.1",
        foodPairing = listOf("Spicy thai curry", "Grilled steak", "Cheesecake"),
        brewersTips = "While the boil is vigorous, ensure you keep a close eye on the temperature."
    )

    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            MaterialTheme {
                BeerDetailContent(
                    beer = sampleBeer,
                    animatedVisibilityScope = this,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    onBackClick = {}
                )
            }
        }
    }
}
