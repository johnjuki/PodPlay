package com.podplay.android.ui.screens.episode_player

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun PodPlayPlayerScreen(
    backDispatcher: OnBackPressedDispatcher,
    podcastPlayer: PodPlayPlayerViewModel = hiltViewModel(),
) {
    val episode = podcastPlayer.currentPlayingEpisode.value
    AnimatedVisibility(
        visible = episode != null && podcastPlayer.showPlayerFullScreen,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        if (episode != null) {
            PodPlayPlayerBody(episode, backDispatcher, podcastPlayer)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PodPlayPlayerBody(
    episode: Episode,
    backDispatcher: OnBackPressedDispatcher,
    podPlayPlayerViewModel: PodPlayPlayerViewModel,
) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val endAnchor = LocalConfiguration.current.screenHeightDp * LocalDensity.current.density
    val anchors = mapOf(
        0f to 0,
        endAnchor to 1
    )

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                podPlayPlayerViewModel.showPlayerFullScreen = false
            }
        }
    }

    val iconResId =
        if (podPlayPlayerViewModel.podcastIsPlaying) R.drawable.ic_pause_white else R.drawable.ic_play_arrow_white

    var sliderIsChanging by remember { mutableStateOf(false) }

    var localSliderValue by remember { mutableStateOf(0f) }

    val sliderProgress =
        if (sliderIsChanging) localSliderValue else podPlayPlayerViewModel.currentEpisodeProgress

    Box(
        modifier = Modifier
            .fillMaxSize()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.34f) },
                orientation = Orientation.Vertical,
            )
    ) {
        if (swipeableState.currentValue >= 1) {
            LaunchedEffect("key") {
                podPlayPlayerViewModel.showPlayerFullScreen = false
            }
        }

        PodPlayPlayerStatelessContent(
            episode = episode,
            darkTheme = isSystemInDarkTheme(),
            yOffset = swipeableState.offset.value.roundToInt(),
            playPauseIcon = iconResId,
            playbackProgress = sliderProgress,
            currentTime = podPlayPlayerViewModel.currentPlaybackFormattedPosition,
            totalTime = podPlayPlayerViewModel.currentEpisodeFormattedDuration,
            onRewind = { podPlayPlayerViewModel.rewind() },
            onForward = { podPlayPlayerViewModel.fastForward() },
            onTogglePlayback = { podPlayPlayerViewModel.togglePlaybackState() },
            onSliderChange = {newPosition ->
                localSliderValue = newPosition
                sliderIsChanging = true
            },
            onSliderChangeFinished = {
                podPlayPlayerViewModel.seekToFraction(localSliderValue)
                sliderIsChanging = false
            }
        ) {
            podPlayPlayerViewModel.showPlayerFullScreen = false
        }
    }

    LaunchedEffect("playbackPosition") {
        podPlayPlayerViewModel.updateCurrentPlaybackPosition()
    }

    DisposableEffect(backDispatcher) {
        backDispatcher.addCallback(backCallback)
        onDispose {
            backCallback.remove()
            podPlayPlayerViewModel.showPlayerFullScreen = false
        }
    }
}

@Composable
fun PodPlayPlayerStatelessContent(
    episode: Episode,
    yOffset: Int,
    @DrawableRes playPauseIcon: Int,
    playbackProgress: Float,
    currentTime: String,
    totalTime: String,
    darkTheme: Boolean,
    onRewind: () -> Unit,
    onForward: () -> Unit,
    onTogglePlayback: () -> Unit,
    onSliderChange: (Float) -> Unit,
    onSliderChangeFinished: () -> Unit,
    onClose: () -> Unit,
) {
    Box(
        modifier = Modifier
            .offset { IntOffset(0, yOffset) }
            .fillMaxSize()
    ) {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {
                Column {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.close),
                            modifier = Modifier
                                .clip(CircleShape)
                                .padding(16.dp)
                        )
                    }

                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 32.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .weight(1f, fill = false)
                                .aspectRatio(1f)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(episode.imageUrl).placeholder(R.drawable.logo).crossfade(true).build(),
                                error = painterResource(R.drawable.logo),
                                contentScale = ContentScale.Fit,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }

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

                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)) {
                            Slider(
                                value = playbackProgress,
                                modifier = Modifier.fillMaxWidth(),
                                onValueChange = onSliderChange,
                                onValueChangeFinished = onSliderChangeFinished,
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(text = currentTime)
                                Text(text = totalTime)
                            }
                        }

                        Row(horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_replay_10_white),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable(onClick = onRewind)
                                    .padding(12.dp)
                                    .size(32.dp)
                            )
                            Icon(
                                painter = painterResource(id = playPauseIcon),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.onBackground)
                                    .clickable(onClick = onTogglePlayback)
                                    .size(64.dp)
                                    .padding(8.dp)
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_forward_30_white),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable(onClick = onForward)
                                    .padding(12.dp)
                                    .size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
