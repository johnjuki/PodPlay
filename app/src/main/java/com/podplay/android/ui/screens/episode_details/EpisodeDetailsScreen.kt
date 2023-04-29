package com.podplay.android.ui.screens.episode_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.podplay.android.R
import com.podplay.android.model.PodcastDummyData
import com.podplay.android.util.DateUtils
import com.podplay.android.util.HtmlUtils

@Composable
fun EpisodeDetailsRoute(
    guid: String,
    navigateUp: () -> Unit,
    viewModel: EpisodeDetailsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.getEpisode(guid)
    }
    EpisodeDetailsScreen(
        navigateUp = navigateUp,
        uiState = viewModel.uiState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeDetailsScreen(
    navigateUp: () -> Unit,
    uiState: EpisodeDetailsUiState,
    modifier: Modifier = Modifier,
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
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
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .verticalScroll(
                        rememberScrollState()
                    ),
            ) {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    val episode = uiState.episode
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(uiState.podcastImageUrl)
                            .placeholder(R.drawable.logo)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier
                            .height(120.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = episode.title, style = MaterialTheme.typography.titleLarge)

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = uiState.podcastName, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(5.dp))

                    Row {
                        Text(text = DateUtils.dateToShortDate(episode.releaseDate))
                        Text(text = " - ")
                        Text(text = episode.duration)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = { /*TODO*/ }, modifier = Modifier.width(200.dp)) {
                        Text(text = stringResource(R.string.play), style = MaterialTheme.typography.labelLarge)
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Text(text = HtmlUtils.htmlToSpannable(episode.description).toString())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodeDetailsScreenPreview() {
    EpisodeDetailsScreen(
        navigateUp = { },
        uiState = EpisodeDetailsUiState(
            episode = PodcastDummyData.podcast.episodes.first(),
            podcastName = PodcastDummyData.podcast.feedTitle,
            podcastImageUrl = PodcastDummyData.podcast.imageUrl
        )
    )
}
