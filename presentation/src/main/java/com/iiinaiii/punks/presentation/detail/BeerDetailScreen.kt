package com.iiinaiii.punks.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.iiinaiii.punks.presentation.R
import com.iiinaiii.punks.presentation.detail.model.BeerDetailUiModel
import com.iiinaiii.punks.presentation.detail.model.BeerDetailUiState

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BeerDetailScreen(
    viewModel: BeerDetailViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
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
                        paddingValues = paddingValues
                    )
                }
                is BeerDetailUiState.Error -> {
                    Text(
                        text = state.exception.message ?: "Unknown error",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun BeerDetailContent(
    beer: BeerDetailUiModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    paddingValues: PaddingValues
) {
    with(sharedTransitionScope) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(beer.imageUrl)
                        .placeholder(R.drawable.placeholder_bottle)
                        .crossfade(true)
                        .precision(Precision.EXACT)
                        .build(),
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
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.sharedElement(
                        sharedContentState = rememberSharedContentState(key = "tagline-${beer.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                )
                Text(
                    text = beer.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Text(
                    text = beer.firstBrewed,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "date-${beer.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )

                DetailSection("Basics") {
                    DetailRow(
                        label = "ABV",
                        value = beer.abv,
                        modifier = Modifier.sharedElement(
                            sharedContentState = rememberSharedContentState(key = "abv-${beer.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
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
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(top = 8.dp)) {
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
        name = "Lost Lager",
        tagline = "Dry-Hopped Pilsner.",
        firstBrewed = "DECEMBER 2018",
        description = "A pilsner that combines the light, crisp and clean lager profile provided by Weihenstephan's house yeast, with the vibrant citrus and stonefruit aromas associated with new German hop Saphir. This lager is easy-going but has subtle depths; toast, hints of spice and a zesty lime marmalade character.",
        imageUrl = "https://punkapi-alxiw.amvera.io/v3/images/366.png",
        abv = "4.5",
        ibu = "37",
        targetOg = "1056",
        targetFg = "1010",
        ebc = "5",
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
                    paddingValues = PaddingValues.Zero
                )
            }
        }
    }
}
