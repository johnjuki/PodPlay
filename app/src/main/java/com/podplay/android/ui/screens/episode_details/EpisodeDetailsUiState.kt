package com.podplay.android.ui.screens.episode_details

import com.podplay.android.model.EpisodeViewData

data class EpisodeDetailsUiState(
    val episodeViewData: EpisodeViewData = EpisodeViewData(),
    val isLoading: Boolean = false,
)
