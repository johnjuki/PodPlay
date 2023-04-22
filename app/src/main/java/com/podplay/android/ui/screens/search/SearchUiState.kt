package com.podplay.android.ui.screens.search

data class SearchUiState(
    val podcastSearchList: List<SearchViewModel.PodcastSummaryViewData> = emptyList(),
    val isSearching : Boolean = false,
)
