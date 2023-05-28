package com.podplay.android.ui.screens.subscriptions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.podplay.android.R
import com.podplay.android.data.model.Podcast
import com.podplay.android.data.model.PodcastDummyData

@Composable
fun SubscriptionsRoute(
    onPodcastClick: (feedUrl: String, imageUrl: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SubscriptionsViewModel = hiltViewModel(),
) {
    SubscriptionsScreen(onPodcastClick = onPodcastClick, uiState = viewModel.uiState.collectAsState().value)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsScreen(
    uiState: SubscriptionsUiState,
    onPodcastClick: (feedUrl: String, imageUrl: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = stringResource(id = R.string.subscriptions)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                ),
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            when (uiState) {
                is SubscriptionsUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is SubscriptionsUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    ) {
                        items(uiState.subscriptions) { podcast ->
                            PodcastItem(podcast = podcast, onItemClicked = onPodcastClick)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PodcastItem(
    podcast: Podcast,
    onItemClicked: (feedUrl: String, imageUrl: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        leadingContent = {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(podcast.imageUrl)
                    .size(102)
                    .placeholder(R.drawable.logo)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
            )
        },
        headlineText = {
            Text(
                text = podcast.feedTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        modifier = modifier.clickable {
            onItemClicked(
                podcast.feedUrl,
                podcast.imageUrl
            )
        }
    )
}

@Preview
@Composable
fun SubscriptionsScreenPreview() {
    SubscriptionsScreen(
        uiState = SubscriptionsUiState.Success(listOf(PodcastDummyData.podcast) ),
        onPodcastClick = {_,_ -> }
    )
}