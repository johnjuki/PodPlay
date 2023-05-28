package com.podplay.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.podplay.android.ui.PodPlayApp
import com.podplay.android.ui.theme.PodplayTheme
import com.podplay.android.worker.EpisodeUpdateWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class PodPlayActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleJobs()
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                PodplayTheme {
                    PodPlayApp(
                        windowSizeClass = calculateWindowSizeClass(activity = this),
                        backDispatcher = onBackPressedDispatcher,
                    )
                }
            }
        }
    }

    private fun scheduleJobs() {
        val constraints: Constraints = Constraints.Builder().apply {
            setRequiredNetworkType(NetworkType.CONNECTED)
            setRequiresCharging(true)
        }.build()
        val request =
            PeriodicWorkRequestBuilder<EpisodeUpdateWorker>(1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(TAG_EPISODE_UPDATE_JOB, ExistingPeriodicWorkPolicy.UPDATE, request)
    }

    companion object {
        private const val TAG_EPISODE_UPDATE_JOB = "com.podplay.android.episodes"
    }
}
