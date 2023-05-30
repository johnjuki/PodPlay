package com.podplay.android.ui.screens.podcast_details

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.podplay.android.R
import com.podplay.android.data.model.Podcast
import com.podplay.android.data.model.PodcastDummyData
import com.podplay.android.ui.common.ClickableDescription
import com.podplay.android.util.DateUtils
import com.podplay.android.util.Description
import com.podplay.android.util.HtmlUtils

@Composable
fun PodcastDetailsRoute(
    feedUrl: String,
    imageUrl: String,
    navigateUp: () -> Unit,
    onEpisodeClick: (guid: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PodcastDetailsViewModel = hiltViewModel(),
) {
    viewModel.updateImageUrl(imageUrl)

    LaunchedEffect(Unit) {
        viewModel.getPodcast(feedUrl)
    }

    PodcastDetailsScreen(
        uiState = viewModel.uiState,
        navigateUp = navigateUp,
        onSubscribeButtonClick = { viewModel.subscribe() },
        onEpisodeClick = onEpisodeClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastDetailsScreen(
    uiState: PodcastDetailsUiState,
    navigateUp: () -> Unit,
    onSubscribeButtonClick: () -> Unit,
    onEpisodeClick: (guid: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                ),
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            if (uiState.isSearching) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.semantics {
                        this.contentDescription = Description.LOADING
                    })
                }
            } else {
                val podcast = uiState.podcast

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp),
                ) {
                    PodcastDetailsHeader(podcast, onSubscribeButtonClick)
                    Spacer(modifier = Modifier.height(10.dp))
                    PodcastDetailsTabRow(podcast, onEpisodeClick, context)
                }
            }
        }
    }
}

@Composable
private fun PodcastDetailsHeader(
    podcast: Podcast,
    onSubscribeButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(102.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(podcast.imageUrl)
                    .placeholder(R.drawable.logo)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp))
                    .semantics {
                        this.contentDescription = Description.PODCAST_IMAGE
                    }
            )
        }
        Spacer(modifier = Modifier.width(15.dp))
        Column {
            Text(text = podcast.feedTitle)
            Spacer(modifier = Modifier.size(10.dp))
            Button(onClick = onSubscribeButtonClick) {
                Text(
                    text = if (podcast.isSubscribed) stringResource(R.string.subscribed)
                    else stringResource(id = R.string.subscribe)
                )
            }
        }
    }
}

@Composable
fun PodcastDetailsTabRow(
    podcast: Podcast,
    onEpisodeClick: (guid: String) -> Unit,
    context: Context,
    modifier: Modifier = Modifier,
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val titles = listOf(stringResource(id = R.string.episodes), stringResource(R.string.description))
    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title) },
                )
            }
        }

        if (selectedTabIndex == 0) {
            EpisodesList(podcast, onEpisodeClick)
        } else {
            PodcastDescription(podcast, modifier, context)
        }
    }
}

@Composable
private fun PodcastDescription(
    podcast: Podcast,
    modifier: Modifier,
    context: Context
) {
    LazyColumn {
        item {
            Text(text = podcast.feedDesc, modifier = modifier.padding(top = 10.dp, bottom = 16.dp))
            ClickableDescription(
                text = podcast.feedDesc,
                context = context,
                modifier = Modifier.padding(top = 10.dp),
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun EpisodesList(
    podcast: Podcast,
    onEpisodeClick: (guid: String) -> Unit
) {
    LazyColumn {
        items(podcast.episodes) { episode ->
            if (!episode.mimeType.startsWith("video")) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(onClick = { onEpisodeClick(episode.guid) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                    ) {
                        Text(
                            text = episode.title, fontWeight = FontWeight.SemiBold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = HtmlUtils.htmlToSpannable(episode.description)
                                .toString(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_access_time_24),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = episode.duration)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = DateUtils.formatTimePassed(episode.releaseDate))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PodcastDetailsScreenPreview() {
    PodcastDetailsScreen(
        uiState = PodcastDetailsUiState(podcast = PodcastDummyData.podcast),
        navigateUp = { },
        onSubscribeButtonClick = { },
        onEpisodeClick = {}
    )
}
