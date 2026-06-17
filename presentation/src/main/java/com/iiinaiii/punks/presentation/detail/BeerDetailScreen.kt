package com.iiinaiii.punks.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
                actions = {
                    val isFavorite = (uiState as? BeerDetailUiState.Success)?.model?.isFavorite ?: return@TopAppBar
                    IconButton(
                        onClick = viewModel::onFavoriteClick,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            painter = if (isFavorite) {
                                painterResource(id = R.drawable.ic_heart_filled)
                            } else {
                                painterResource(id = R.drawable.ic_heart_outline)
                            },
                            contentDescription = "Favorite",
                            tint = if (isFavorite) colorResource(R.color.favorite) else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun BeerDetailContent(
    beer: BeerDetailUiModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    paddingValues: PaddingValues
) {
    with(sharedTransitionScope) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // top section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                )

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = paddingValues.calculateTopPadding()),
                    verticalAlignment = Alignment.CenterVertically
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
                            .size(width = 160.dp, height = 360.dp)
                            .padding(top = 16.dp)
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(key = "image-${beer.id}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        contentScale = ContentScale.Fit
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 8.dp, start = 4.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = beer.firstBrewed,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState(key = "date-${beer.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            StatItem(
                                title = stringResource(R.string.abv),
                                value = beer.abv,
                                modifier = Modifier.sharedElement(
                                    sharedContentState = rememberSharedContentState(key = "abv-${beer.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                            )
                            StatItem(
                                title = stringResource(R.string.ibu),
                                value = beer.ibu
                            )
                            StatItem(
                                title = stringResource(R.string.og),
                                value = beer.targetOg
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // title
                Text(
                    text = beer.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.sharedElement(
                        sharedContentState = rememberSharedContentState(key = "name-${beer.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                )

                // tagline
                Text(
                    text = beer.tagline,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "tagline-${beer.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )

                // description
                DetailSection(stringResource(R.string.header_description)) {
                    Text(
                        text = beer.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // basics
                DetailSection(stringResource(R.string.header_basics)) {
                    DetailRow(stringResource(R.string.volume), beer.volume)
                    DetailRow(stringResource(R.string.boil_volume), beer.boilVolume)
                    DetailRow(stringResource(R.string.abv), beer.abv)
                    DetailRow(stringResource(R.string.target_fg), beer.targetFg)
                    DetailRow(stringResource(R.string.target_og), beer.targetOg)
                    DetailRow(stringResource(R.string.ebc), beer.ebc)
                    DetailRow(stringResource(R.string.srm), beer.srm)
                    DetailRow(stringResource(R.string.ph), beer.ph)
                    DetailRow(stringResource(R.string.attenuation_level), beer.attenuationLevel)
                }

                // food pairing
                DetailSection(stringResource(R.string.header_food_pairing)) {
                    beer.foodPairing.forEach { food ->
                        Text(
                            text = food,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        )
                    }
                }

                // tips
                DetailSection(stringResource(R.string.header_brewers_tips)) {
                    Text(
                        text = beer.brewersTips,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
                    )
                }

                // contributed by
                DetailSection(stringResource(R.string.header_contributed_by)) {
                    Text(
                        text = beer.contributedBy,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(paddingValues.calculateBottomPadding())
            )
        }
    }
}

@Composable
private fun StatItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun DetailSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        content()
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
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
        brewersTips = "While the boil is vigorous, ensure you keep a close eye on the temperature.",
        contributedBy = "Alexander Ivanovsky <alxiw>"
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
