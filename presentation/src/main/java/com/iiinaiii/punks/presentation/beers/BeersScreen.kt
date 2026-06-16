package com.iiinaiii.punks.presentation.beers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.iiinaiii.punks.presentation.R
import com.iiinaiii.punks.presentation.beers.model.BeerItemUiModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BeersScreen(
    viewModel: BeersViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onBeerClick: (Int) -> Unit,
) {
    val beers = viewModel.beersFlow.collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxSize()) {
        // use beers.loadState.mediator?.refresh with higher priority when use remote mediator
        when (val refreshState = beers.loadState.refresh) {
            is LoadState.Loading -> BeersLoading(modifier = Modifier.align(Alignment.Center))
            is LoadState.Error -> BeersError(
                message = refreshState.error.localizedMessage ?: "Unknown error",
                onRetry = { beers.retry() },
                modifier = Modifier.align(Alignment.Center)
            )
            is LoadState.NotLoading -> {
                if (beers.itemCount == 0) {
                    BeersEmpty(modifier = Modifier.align(Alignment.Center))
                } else {
                    BeersList(
                        beers = beers,
                        animatedVisibilityScope = animatedVisibilityScope,
                        sharedTransitionScope = sharedTransitionScope,
                        onBeerClick = onBeerClick
                    )
                }
            }
        }
    }
}

@Composable
private fun BeersLoading(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier)
}

@Composable
private fun BeersEmpty(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.empty),
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun BeersError(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun BeersList(
    beers: LazyPagingItems<BeerItemUiModel>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onBeerClick: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = beers.itemCount,
            key = beers.itemKey { it.id },
            contentType = beers.itemContentType { "beer" }
        ) { index ->
            beers[index]?.let { beer ->
                BeerItem(
                    beer = beer,
                    animatedVisibilityScope = animatedVisibilityScope,
                    sharedTransitionScope = sharedTransitionScope,
                    onClick = { onBeerClick(beer.id) }
                )
                HorizontalDivider()
            }
        }

        when (val appendState = beers.loadState.append) {
            is LoadState.Loading -> item { LoadingItem() }
            is LoadState.Error -> item { ErrorItem(appendState.error) { beers.retry() } }
            else -> Unit
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun BeerItem(
    beer: BeerItemUiModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
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
                .size(width = 50.dp, height = 75.dp)
                .sharedElementIfNotPreview(
                    sharedTransitionScope = sharedTransitionScope,
                    key = "image-${beer.id}",
                    animatedVisibilityScope = animatedVisibilityScope
                ),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = beer.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .sharedElementIfNotPreview(
                        sharedTransitionScope = sharedTransitionScope,
                        key = "name-${beer.id}",
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
            )
            Text(
                text = beer.tagline,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .sharedElementIfNotPreview(
                        sharedTransitionScope = sharedTransitionScope,
                        key = "tagline-${beer.id}",
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = beer.abv,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .width(50.dp)
                        .sharedElementIfNotPreview(
                            sharedTransitionScope = sharedTransitionScope,
                            key = "abv-${beer.id}",
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )

                Text(
                    text = beer.firstBrewed,
                    modifier = Modifier
                        .weight(1f)
                        .sharedElementIfNotPreview(
                            sharedTransitionScope = sharedTransitionScope,
                            key = "date-${beer.id}",
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "#${beer.id}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorItem(
    error: Throwable,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = error.localizedMessage ?: "Unknown error",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            Button(
                onClick = onClick,
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Text(
                    text = stringResource(R.string.retry),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedElementIfNotPreview(
    sharedTransitionScope: SharedTransitionScope,
    key: Any,
    animatedVisibilityScope: AnimatedVisibilityScope
): Modifier {
    val isPreview = LocalInspectionMode.current
    return if (isPreview) this
    else with(sharedTransitionScope) {
        this@sharedElementIfNotPreview.sharedElement(
            sharedContentState = rememberSharedContentState(key = key),
            animatedVisibilityScope = animatedVisibilityScope
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BeersLoadingPreview() {
    MaterialTheme {
        BeersLoading()
    }
}

@Preview(showBackground = true)
@Composable
private fun BeersEmptyPreview() {
    MaterialTheme {
        BeersEmpty()
    }
}

@Preview(showBackground = true)
@Composable
private fun BeersErrorPreview() {
    MaterialTheme {
        BeersError(
            message = "Something went wrong",
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingItemPreview() {
    MaterialTheme {
        LoadingItem()
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorItemPreview() {
    MaterialTheme {
        ErrorItem(
            error = Exception("Something went wrong"),
            onClick = {}
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun BeerItemPreview() {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            MaterialTheme {
                BeerItem(
                    beer = BeerItemUiModel(
                        id = 366,
                        name = "Lost Lager",
                        tagline = "Dry-Hopped Pilsner.",
                        firstBrewed = "DECEMBER 2018",
                        imageUrl = "https://punkapi-alxiw.amvera.io/v3/images/001.png",
                        abv = "4.5%"
                    ),
                    animatedVisibilityScope = this@AnimatedVisibility,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    onClick = {}
                )
            }
        }
    }
}
