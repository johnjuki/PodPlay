package com.podplay.android.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.podplay.android.R
import com.podplay.android.db.PodPlayDatabase
import com.podplay.android.repository.PodcastRepo
import com.podplay.android.service.RssFeedService
import com.podplay.android.ui.PodcastActivity

class EpisodeUpdateWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = coroutineScope {
        val job = async {
            val db = PodPlayDatabase.getInstance(applicationContext, this)
            val repo = PodcastRepo(RssFeedService.instance, db.podcastDao())
            val podcastUpdates = repo.updatePodcastEpisodes()
            if (Build.VERSION_CODES.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            }
            for (podcastUpdate in podcastUpdates) {
                displayNotification(podcastUpdate)
            }
        }
        job.await()
        Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.getNotificationChannel(EPISODE_CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                EPISODE_CHANNEL_ID,
                "Episodes",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun displayNotification(podcastInfo: PodcastRepo.PodcastUpdateInfo) {
        val contentIntent = Intent(applicationContext, PodcastActivity::class.java)
        contentIntent.putExtra(EXTRA_FEED_URL, podcastInfo.feedUrl)
        val pendingContentIntent = PendingIntent.getActivity(applicationContext, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification =
            NotificationCompat
                .Builder(applicationContext, EPISODE_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_episode_icon)
                .setContentTitle(applicationContext.getString(R.string.episode_notification_title))
                .setContentText(applicationContext.getString(R.string.episode_notification_text, podcastInfo.newCount, podcastInfo.name))
                .setNumber(podcastInfo.newCount)
                .setAutoCancel(true)
                .setContentIntent(pendingContentIntent)
                .build()
        val notificationManger = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManger.notify(podcastInfo.name, 0, notification)
    }

    companion object {
        const val EPISODE_CHANNEL_ID = "podplay_episodes_channel"
        const val EXTRA_FEED_URL = "PodcastFeedUrl"
    }
}
