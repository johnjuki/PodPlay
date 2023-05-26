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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.podplay.android.R
import com.podplay.android.ui.compose.PodcastImage
import com.podplay.android.ui.screens.episode_player.PodPlayPlayerViewModel
import com.podplay.android.util.DateUtils
import com.podplay.android.util.HtmlUtils

@Composable
fun EpisodeDetailsRoute(
    guid: String,
    navigateUp: () -> Unit,
    episodeDetailsViewModel: EpisodeDetailsViewModel = hiltViewModel(),
    podPlayPlayerViewModel: PodPlayPlayerViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        episodeDetailsViewModel.getEpisode(guid)
    }
    EpisodeDetailsScreen(
        navigateUp = navigateUp,
        uiState = episodeDetailsViewModel.uiState,
        podPlayPlayerViewModel = podPlayPlayerViewModel,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeDetailsScreen(
    navigateUp: () -> Unit,
    uiState: EpisodeDetailsUiState,
    podPlayPlayerViewModel: PodPlayPlayerViewModel,
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
            modifier = Modifier.padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    val episode = uiState.episode
                    val playButtonText =
                        if (podPlayPlayerViewModel.podcastIsPlaying &&
                            podPlayPlayerViewModel.currentPlayingEpisode.value?.guid == episode.guid
                        ) stringResource(id = R.string.pause) else stringResource(id = R.string.play)

                    PodcastImage(url = episode.imageUrl ?: "", modifier = Modifier.height(120.dp))

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = episode.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = episode.podcastName ?: "", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(5.dp))

                    Row {
                        Text(text = DateUtils.dateToMonthDayYear(episode.releaseDate))
                        Text(text = " • ")
                        Text(text = episode.duration)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = {
                        podPlayPlayerViewModel.playPodcast(listOf(episode), episode)
                    }, modifier = Modifier.width(200.dp)) {
                        Text(
                            text = playButtonText,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = HtmlUtils.htmlToSpannable(episode.description).toString())
                }
            }
        }
    }
}
