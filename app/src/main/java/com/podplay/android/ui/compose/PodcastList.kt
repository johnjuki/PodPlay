package com.podplay.android.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.podplay.android.R
import com.podplay.android.ui.screens.search.SearchViewModel

@Composable
fun PodcastList(
    podcastList: List<SearchViewModel.PodcastSummaryViewData>,
    onItemClick: (feedUrl: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn {
        items(podcastList) { podcast ->
            PodcastItem(podcast = podcast, onItemClicked = onItemClick)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastItem(
    podcast: SearchViewModel.PodcastSummaryViewData,
    onItemClicked: (feedUrl: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        leadingContent = {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(podcast.imageUrl)
                    .placeholder(R.drawable.logo)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.logo),
                contentDescription = null,
            )
        },
        headlineText = { Text(text = podcast.name!!) },
        modifier = Modifier.clickable {
            onItemClicked(
                podcast.feedUrl!!,
            )
        }
    )
}
