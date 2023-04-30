package com.podplay.android.ui.screens.episode_details

import com.podplay.android.data.model.Episode

data class EpisodeDetailsUiState(
    val episode: Episode = Episode(),
    val isLoading: Boolean = false,
)
