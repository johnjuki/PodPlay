package com.podplay.android.ui.screens.episode_details

import com.podplay.android.model.Episode

data class EpisodeDetailsUiState(
    val episode: Episode = Episode(),
    val podcastName: String = "",
    val podcastImageUrl : String = "",
    val isLoading: Boolean = false,
)
