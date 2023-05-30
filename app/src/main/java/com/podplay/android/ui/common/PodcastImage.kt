package com.podplay.android.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.podplay.android.R
import com.podplay.android.util.Description

@Composable
fun PodcastImage(
    url: String,
    modifier : Modifier = Modifier,
    aspectRatio : Float = 1f,
) {
    Box(
        modifier
            .clip(MaterialTheme.shapes.medium)
            .aspectRatio(aspectRatio)
            .background(Color.Red)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(url)
                .placeholder(R.drawable.logo).crossfade(true).build(),
            error = painterResource(id = R.drawable.logo),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.podcast_thumbnail),
            modifier = modifier
                .fillMaxSize()
                .semantics { contentDescription = Description.PODCAST_IMAGE }
        )
    }
}
