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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.podplay.android.R
import com.podplay.android.data.model.Episode
import com.podplay.android.ui.common.ClickableDescription
import com.podplay.android.ui.common.PodcastImage
import com.podplay.android.ui.screens.episode_player.PodPlayPlayerViewModel
import com.podplay.android.util.DateUtils
import com.podplay.android.util.Description
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
    val context = LocalContext.current
    EpisodeDetailsScreen(
        navigateUp = navigateUp,
        uiState = episodeDetailsViewModel.uiState,
        playPauseText = { if (podPlayPlayerViewModel.podcastIsPlaying &&
            podPlayPlayerViewModel.currentPlayingEpisode.value?.guid == it
        ) context.getString(R.string.pause) else context.getString(R.string.play) },
        onPlayPauseClick = { episode ->
            podPlayPlayerViewModel.playPodcast(listOf(episode), episode)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeDetailsScreen(
    navigateUp: () -> Unit,
    uiState: EpisodeDetailsUiState,
    onPlayPauseClick : (episode: Episode) -> Unit,
    modifier: Modifier = Modifier,
    playPauseText: (text: String) -> String,
) {

    val context = LocalContext.current
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
                        CircularProgressIndicator(modifier = Modifier.semantics {
                            contentDescription = Description.LOADING
                        })
                    }
                } else {
                    val episode = uiState.episode

                    PodcastImage(url = episode.imageUrl ?: "", modifier = Modifier.height(120.dp))

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = episode.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = episode.podcastName ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Row {
                        Text(text = DateUtils.dateToMonthDayYear(episode.releaseDate))
                        Text(text = " • ")
                        Text(text = episode.duration)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = { onPlayPauseClick(episode) }, modifier = Modifier.width(200.dp)) {
                        Text(
                            text = playPauseText(episode.guid) ,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    ClickableDescription(
                        text = HtmlUtils.htmlToSpannable(episode.description).toString(),
                        context = context,
                    )
                }
            }
        }
    }
}
