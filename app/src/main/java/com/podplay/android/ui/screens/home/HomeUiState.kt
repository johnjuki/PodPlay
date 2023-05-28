package com.podplay.android.ui.screens.home

import com.podplay.android.data.model.PodcastSummaryViewData

data class HomeUiState(
    val healthList: List<PodcastSummaryViewData> = emptyList(),
    val selfImprovementList: List<PodcastSummaryViewData> = emptyList(),
    val techList: List<PodcastSummaryViewData> = emptyList(),
    val businessList : List<PodcastSummaryViewData> = emptyList(),
    val foodList: List<PodcastSummaryViewData> = emptyList(),
    val isLoading: Boolean = true,
    val showError: Boolean = false,
)
