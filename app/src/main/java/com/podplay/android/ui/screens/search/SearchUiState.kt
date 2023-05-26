package com.podplay.android.ui.screens.search

import com.podplay.android.data.model.RecentSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchUiState(
    val podcastSearchList: List<SearchViewModel.PodcastSummaryViewData> = emptyList(),
    val recentSearchFlow: Flow<List<RecentSearch>> = emptyFlow(),
    val isRecentSearchVisible : Boolean = true,
    val showSearchResults: Boolean = false,
    val refreshing : Boolean = false,
)
