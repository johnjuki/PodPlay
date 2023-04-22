package com.podplay.android.ui.screens.podcast_details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.podplay.android.R
import com.podplay.android.util.DateUtils
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
        onEpisodeClick = onEpisodeClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastDetailsScreen(
    uiState: PodcastDetailsUiState,
    navigateUp: () -> Unit,
    onEpisodeClick: (guid: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "")
                },
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
                    CircularProgressIndicator()
                }
            } else {
                val podcast = uiState.podcast

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp),
                ) {
                    LazyColumn {

                        item {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(podcast.imageUrl)
                                        .placeholder(R.drawable.logo)
                                        .crossfade(true)
                                        .build(),
                                    error = painterResource(R.drawable.logo),
                                    contentDescription = null,
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = podcast.feedTitle)
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Button(onClick = { /*TODO*/ }) {
                                        Text(text = stringResource(R.string.subscribe))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(text = stringResource(R.string.episodes))
                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        items(podcast.episodes) { episode ->
                            Column(modifier = Modifier.clickable {
                                onEpisodeClick(episode.guid)
                            }) {
                                Text(text = episode.title, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = HtmlUtils.htmlToSpannable(episode.description).toString(),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row {
                                    Text(text = DateUtils.dateToShortDate(episode.releaseDate))
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(text = episode.duration)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Divider()
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
