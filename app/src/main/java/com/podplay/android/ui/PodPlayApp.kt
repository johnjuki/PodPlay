package com.podplay.android.ui

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.podplay.android.navigation.PodPlayNavHost
import com.podplay.android.ui.screens.episode_player.EpisodePlayerScreen
import com.podplay.android.ui.screens.episode_player.PodPlayBottomBar

@Composable
fun PodPlayApp(backDispatcher : OnBackPressedDispatcher) {
    Box(modifier = Modifier.fillMaxWidth()) {
        PodPlayNavHost()
        PodPlayBottomBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        EpisodePlayerScreen(backDispatcher = backDispatcher)
    }
}
