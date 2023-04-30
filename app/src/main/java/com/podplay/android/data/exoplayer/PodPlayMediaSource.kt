package com.podplay.android.data.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.podplay.android.data.model.Episode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PodPlayMediaSource @Inject constructor() {

    var mediaMetadataEpisodes : List<MediaMetadataCompat> = emptyList()
    var podcastEpisodes : List<Episode> = emptyList()
        private set

    private val onReadyListeners = mutableListOf<OnReadyListener>()

    private val isReady : Boolean
        get() = state == MusicSourceState.INITIALIZED

    private var state : MusicSourceState =
        MusicSourceState.CREATED
        set(value) {
            if (value == MusicSourceState.INITIALIZED || value == MusicSourceState.ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(isReady)
                    }
                }
            } else {
                field = value
            }
        }

    fun setEpisodes(data: List<Episode>) {
        state = MusicSourceState.INITIALIZING
        podcastEpisodes = data
        mediaMetadataEpisodes = data.map { episode->
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, episode.guid)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, episode.podcastName)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, episode.title)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, episode.title)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, episode.mediaUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, episode.imageUrl)
                .build()
        }
        state = MusicSourceState.INITIALIZED
    }

    fun asMediaSource(dataSourceFactory: DataSource.Factory) : ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        mediaMetadataEpisodes.forEach { mediaMetadataCompat ->
            val mediaItem = MediaItem.fromUri(
                mediaMetadataCompat.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri()
            )
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = mediaMetadataEpisodes.map { mediaMetadataCompat ->
        val description = MediaDescriptionCompat.Builder()
            .setMediaId(mediaMetadataCompat.description.mediaId)
            .setTitle(mediaMetadataCompat.description.title)
            .setSubtitle(mediaMetadataCompat.description.subtitle)
            .setIconUri(mediaMetadataCompat.description.iconUri)
            .setMediaUri(mediaMetadataCompat.description.mediaUri)
            .build()
        MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
    }.toMutableList()

    fun whenReady(listener: OnReadyListener) : Boolean {
        return if (state == MusicSourceState.CREATED || state == MusicSourceState.INITIALIZING) {
            onReadyListeners += listener
            false
        } else {
            listener(isReady)
            true
        }
    }

    fun refresh() {
        onReadyListeners.clear()
        state = MusicSourceState.CREATED
    }
}

typealias OnReadyListener = (Boolean) -> Unit

enum class MusicSourceState {
    CREATED,
    INITIALIZING,
    INITIALIZED,
    ERROR
}
