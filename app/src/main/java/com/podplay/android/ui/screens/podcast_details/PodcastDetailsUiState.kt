package com.podplay.android.ui.screens.podcast_details

import com.podplay.android.data.model.Podcast

data class PodcastDetailsUiState(
    val podcast: Podcast = Podcast(),
    val imageUrl : String = "",
    val isSearching: Boolean = false,
)
