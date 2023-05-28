package com.podplay.android.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.podplay.android.data.model.RecentSearch
import com.podplay.android.data.model.itunesPodcastToPodcastSummaryView
import com.podplay.android.repository.ItunesRepo
import com.podplay.android.repository.RecentSearchRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val iTunesRepo: ItunesRepo,
    private val recentSearchRepo: RecentSearchRepo,
): ViewModel() {

    var searchUiState by mutableStateOf(SearchUiState())

    var searchQuery by mutableStateOf("")
        private set

    init {
        getRecentSearch()
    }

    fun updateSearchQuery(searchQuery: String) {
        this.searchQuery = searchQuery
    }

    fun clearSearch() {
        updateSearchQuery("")
    }

    private fun getRecentSearch() {
        searchUiState = searchUiState.copy(
            recentSearchFlow = recentSearchRepo.getAllRecentSearch()
        )
    }

    fun saveSearchTerm(searchTerm: RecentSearch) = viewModelScope.launch {
        recentSearchRepo.insertRecentSearch(searchTerm)
    }

    fun updateRecentSearchVisibility(isVisible: Boolean) {
        searchUiState = searchUiState.copy(isRecentSearchVisible = isVisible)
    }

    fun searchPodcasts() = viewModelScope.launch {
        searchUiState = searchUiState.copy(showSearchResults = false, refreshing = true)
        val results = iTunesRepo.searchByTerm(searchQuery)

        if (results.isSuccessful) {
            val podcasts = results.body()?.results
            if (!podcasts.isNullOrEmpty()) {
                val podcastSearchList = podcasts.map { podcast ->
                    itunesPodcastToPodcastSummaryView(podcast)
                }
                searchUiState = searchUiState.copy(
                    showSearchResults = true,
                    refreshing = false,
                    podcastSearchList = podcastSearchList,
                )
            }
        } else {
            searchUiState = searchUiState.copy(refreshing = false, podcastSearchList = emptyList()) // TODO: Update to show Error. No search result
        }
    }

    fun updateShowSearchResult(showSearchResults: Boolean) {
        searchUiState = searchUiState.copy(showSearchResults = showSearchResults)
    }

    fun deleteSearchTerm(searchTerm: String) = viewModelScope.launch {
        recentSearchRepo.deleteRecentSearch(searchTerm)
    }

    fun clearSearchHistory() = viewModelScope.launch {
        recentSearchRepo.clearSearchHistory()
    }
}
