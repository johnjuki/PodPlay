package com.podplay.android.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.podplay.android.data.model.Podcast
import com.podplay.android.ui.common.PodcastImage

@Composable
fun PodcastView(
    podcast: Podcast,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() }
    ) {
        PodcastImage(url = podcast.imageUrl, aspectRatio = 1f,)
        Text(
            podcast.feedTitle,
            modifier = Modifier.padding(8.dp)
        )
    }
}
