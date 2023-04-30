package com.podplay.android.data.service

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.podplay.android.MainActivity
import com.podplay.android.data.exoplayer.MediaPlaybackPreparer
import com.podplay.android.data.exoplayer.MediaPlayerNotificationListener
import com.podplay.android.data.exoplayer.MediaPlayerNotificationManager
import com.podplay.android.data.exoplayer.MediaPlayerQueueNavigator
import com.podplay.android.data.exoplayer.PodPlayMediaSource
import com.podplay.android.util.Constants.ACTION_PODCAST_NOTIFICATION_CLICK
import com.podplay.android.util.Constants.EMPTY_ROOT_MEDIA_ID
import com.podplay.android.util.Constants.REFRESH_MEDIA_BROWSER_CHILDREN
import com.podplay.android.util.Constants.START_MEDIA_PLAYBACK_ACTION
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import javax.inject.Inject

@AndroidEntryPoint
class MediaPlayerService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: CacheDataSource.Factory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var mediaSource: PodPlayMediaSource

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession : MediaSessionCompat
    private lateinit var mediaSessionConnector : MediaSessionConnector

    private lateinit var mediaPlayerNotificationManager: MediaPlayerNotificationManager

    private var currentPlayingMedia: MediaMetadataCompat? = null

    private var isPlayerInitialized = false

    var isForegroundService: Boolean = false

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate called")
        val activityPendingIntent = Intent(this, MainActivity::class.java)
            .apply {
                action = ACTION_PODCAST_NOTIFICATION_CLICK
            }.let {
                PendingIntent.getActivity(
                    this,
                    0,
                    it,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }

        mediaSession = MediaSessionCompat(this, TAG).apply {
            setSessionActivity(activityPendingIntent)
            isActive = true
        }

        val mediaPlaybackPreparer = MediaPlaybackPreparer(mediaSource) { mediaMetadataCompat ->
            currentPlayingMedia = mediaMetadataCompat
            preparePlayer(mediaSource.mediaMetadataEpisodes, mediaMetadataCompat, true)
        }

        mediaSessionConnector = MediaSessionConnector(mediaSession).apply {
            setPlaybackPreparer(mediaPlaybackPreparer)
            setQueueNavigator(MediaPlayerQueueNavigator(mediaSession, mediaSource))
            setPlayer(exoPlayer)
        }

        this.sessionToken = mediaSession.sessionToken

        mediaPlayerNotificationManager = MediaPlayerNotificationManager(
            this,
            mediaSession.sessionToken,
            MediaPlayerNotificationListener(this)
        ) {
            currentDuration = exoPlayer.duration
        }
    }

    override fun onCustomAction(action: String, extras: Bundle?, result: Result<Bundle>) {
        super.onCustomAction(action, extras, result)
        when (action) {
            START_MEDIA_PLAYBACK_ACTION -> {
                mediaPlayerNotificationManager.showNotification(exoPlayer)
            }
            REFRESH_MEDIA_BROWSER_CHILDREN -> {
                mediaSource.refresh()
                notifyChildrenChanged(EMPTY_ROOT_MEDIA_ID)
            }
            else -> Unit
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(EMPTY_ROOT_MEDIA_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        Log.i(TAG, "onLoadChildren called")
        when (parentId) {
            EMPTY_ROOT_MEDIA_ID -> {
                val resultsSent = mediaSource.whenReady {isInitialized ->
                    if (isInitialized) {
                        result.sendResult(mediaSource.asMediaItems())
                        if (!isPlayerInitialized && mediaSource.mediaMetadataEpisodes.isNotEmpty()) {
                            isPlayerInitialized = true
                        }
                    } else {
                        result.sendResult(null)
                    }
                }
                if (!resultsSent) result.detach()
            }
            else -> Unit
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        exoPlayer.release()
    }

    private fun preparePlayer(
        mediaMetaDataEpisodes : List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playWhenReady: Boolean,
    ) {
        val indexToPlay = if (currentPlayingMedia == null) 0 else mediaMetaDataEpisodes.indexOf(itemToPlay)
        exoPlayer.setMediaSource(mediaSource.asMediaSource(dataSourceFactory))
        exoPlayer.prepare()
        exoPlayer.seekTo(indexToPlay, 0L)
        exoPlayer.playWhenReady = playWhenReady
    }

    companion object {
        private const val TAG = "MediaPlayerService"

        var currentDuration : Long = 0L
            private set
    }
}
