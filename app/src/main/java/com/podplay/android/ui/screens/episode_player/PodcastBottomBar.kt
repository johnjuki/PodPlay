package com.podplay.android.ui.screens.episode_player

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.podplay.android.R
import com.podplay.android.data.model.Episode
import kotlin.math.roundToInt

@Composable
fun PodPlayBottomBar(
    modifier: Modifier = Modifier,
    episodePlayer : EpisodePlayerViewModel = hiltViewModel(),
) {
    val episode = episodePlayer.currentPlayingEpisode.value

    AnimatedVisibility(visible = episode != null, modifier = modifier) {
        if (episode != null) {
            PodcastBottomBarContent(episode, episodePlayer)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PodcastBottomBarContent(episode: Episode, episodePlayer: EpisodePlayerViewModel) {
    val swipeableState = rememberSwipeableState(0)
    val endAnchor = LocalConfiguration.current.screenWidthDp * LocalDensity.current.density
    val anchors = mapOf(
        0f to 0,
        endAnchor to 1
    )
    val iconResId =
        if (episodePlayer.podcastIsPlaying) R.drawable.ic_pause_white else R.drawable.ic_play_arrow_white

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.54f) },
                orientation = Orientation.Horizontal
            )
    ) {
        if (swipeableState.currentValue >= 1) {
            LaunchedEffect("key") {
                episodePlayer.stopPlayback()
            }
        }

        PodcastBottomBarStatelessContent(
            episode = episode,
            xOffset = swipeableState.offset.value.roundToInt(),
            darkTheme = isSystemInDarkTheme(),
            icon = iconResId,
            onTogglePlaybackState = {
                episodePlayer.togglePlaybackState()
            }
        ) {
            episodePlayer.showPlayerFullScreen = true
        }
    }
}

@Composable
fun PodcastBottomBarStatelessContent(
    episode: Episode,
    xOffset: Int,
    darkTheme: Boolean,
    @DrawableRes icon: Int,
    onTogglePlaybackState: () -> Unit,
    onTap: (Offset) -> Unit,
) {
    Box(
        modifier = Modifier
            .offset { IntOffset(xOffset, 0) }
            .background(if (darkTheme) Color(0xFF343434) else Color(0xFFF1F1F1))
            .navigationBarsPadding()
            .height(64.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onTap = onTap)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(episode.imageUrl)
                    .placeholder(R.drawable.logo).crossfade(true).build(),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.podcast_thumbnail),
                modifier = Modifier.size(64.dp)
            )

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(8.dp),
            ) {
                Text(
                    episode.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    episode.podcastName ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Icon(
                painter = painterResource(icon),
                contentDescription = stringResource(R.string.play),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onTogglePlaybackState)
                    .padding(6.dp)
            )
        }
    }
}
