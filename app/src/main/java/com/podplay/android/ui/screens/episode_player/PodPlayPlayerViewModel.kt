package com.podplay.android.ui.screens.episode_player

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.podplay.android.data.model.Episode
import com.podplay.android.data.service.MediaPlayerService
import com.podplay.android.data.service.MediaPlayerServiceConnection
import com.podplay.android.util.Constants.EMPTY_ROOT_MEDIA_ID
import com.podplay.android.util.Constants.PLAYBACK_POSITION_UPDATE_INTERVAL
import com.podplay.android.util.currentPosition
import com.podplay.android.util.isPlayEnabled
import com.podplay.android.util.isPlaying
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PodPlayPlayerViewModel @Inject constructor(
    private val serviceConnection: MediaPlayerServiceConnection,
) : ViewModel() {

    val currentPlayingEpisode = serviceConnection.currentPlayingEpisode

    var showPlayerFullScreen by mutableStateOf(false)

    var currentPlaybackPosition by mutableStateOf(0L)

    private val playbackState = serviceConnection.playbackState

    private val currentEpisodeDuration: Long
        get() = MediaPlayerService.currentDuration

    val podcastIsPlaying : Boolean
        get() = playbackState.value?.isPlaying == true

    val currentEpisodeProgress: Float
        get() {
            if (currentEpisodeDuration > 0) {
                return currentPlaybackPosition.toFloat() / currentEpisodeDuration
            }
            return 0f
        }

    val currentPlaybackFormattedPosition : String
        get() = formatLong(currentPlaybackPosition)

    val currentEpisodeFormattedDuration: String
        get() = formatLong(currentEpisodeDuration)

    fun playPodcast(episodes: List<Episode>, currentEpisode: Episode) {
        serviceConnection.playPodcast(episodes)
        if (currentEpisode.guid == currentPlayingEpisode.value?.guid) {
            if (podcastIsPlaying) {
                serviceConnection.transportControls.pause()
            } else {
                serviceConnection.transportControls.play()
            }
        } else {
            serviceConnection.transportControls.playFromMediaId(currentEpisode.guid, null)
        }
    }

    fun togglePlaybackState() {
        when {
            playbackState.value?.isPlaying == true -> {
                serviceConnection.transportControls.pause()
            }
            playbackState.value?.isPlayEnabled == true -> {
                serviceConnection.transportControls.play()
            }
        }
    }

    fun stopPlayback() {
        serviceConnection.transportControls.stop()
    }

    fun fastForward() {
        serviceConnection.fastForward()
    }

    fun rewind() {
        serviceConnection.rewind()
    }

    fun seekToFraction(value: Float) {
        serviceConnection.transportControls.seekTo(
            (currentEpisodeDuration * value).toLong()
        )
    }

    suspend fun updateCurrentPlaybackPosition() {
        val currentPosition = playbackState.value?.currentPosition
        if (currentPosition != null && currentPosition != currentPlaybackPosition) {
            currentPlaybackPosition = currentPosition
        }
        delay(PLAYBACK_POSITION_UPDATE_INTERVAL)
        updateCurrentPlaybackPosition()
    }

    private fun formatLong(durationInMillis: Long) : String {
        val hours = TimeUnit.MILLISECONDS.toHours(durationInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()
        serviceConnection.unsubscribe(
            EMPTY_ROOT_MEDIA_ID,
            object: MediaBrowserCompat.SubscriptionCallback() {}
        )
    }

}
