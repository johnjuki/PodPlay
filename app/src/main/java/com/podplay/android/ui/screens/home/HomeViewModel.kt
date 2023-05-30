package com.podplay.android.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.podplay.android.data.model.itunesPodcastToPodcastSummaryView
import com.podplay.android.repository.ItunesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val itunesRepo: ItunesRepo,
) : ViewModel() {

    var homeUiState by mutableStateOf(HomeUiState())

    init {
        searchPodcasts()
    }

    private fun searchPodcasts() = viewModelScope.launch {
        val healthResults = itunesRepo.searchByTerm("health")
        val selfImprovementResults = itunesRepo.searchByTerm("self improvement")
        val techResults = itunesRepo.searchByTerm("technology")
        val businessResults = itunesRepo.searchByTerm("business")
        val foodResults = itunesRepo.searchByTerm("food")

        if (
            healthResults.isSuccessful
            && selfImprovementResults.isSuccessful
            && techResults.isSuccessful
            && businessResults.isSuccessful
            && foodResults.isSuccessful
        ) {
            val healthPodcasts = healthResults.body()?.results
            val selfImprovementPodcasts = selfImprovementResults.body()?.results
            val techPodcasts = techResults.body()?.results
            val businessPodcasts = businessResults.body()?.results
            val foodPodcasts = foodResults.body()?.results

            if (
                !healthPodcasts.isNullOrEmpty()
                && !selfImprovementPodcasts.isNullOrEmpty()
                && !techPodcasts.isNullOrEmpty()
                && !businessPodcasts.isNullOrEmpty()
                && !foodPodcasts.isNullOrEmpty()
            ) {
                val healthList = healthPodcasts.map { podcast ->
                    itunesPodcastToPodcastSummaryView(podcast)
                }
                val selfImprovementList = selfImprovementPodcasts.map { podcast ->
                    itunesPodcastToPodcastSummaryView(podcast)
                }
                val techList = techPodcasts.map { podcast ->
                    itunesPodcastToPodcastSummaryView(podcast)
                }
                val businessList = businessPodcasts.map { podcast ->
                    itunesPodcastToPodcastSummaryView(podcast)
                }
                val foodList = foodPodcasts.map { podcast ->
                    itunesPodcastToPodcastSummaryView(podcast)
                }
                homeUiState = homeUiState.copy(
                    healthList = healthList,
                    selfImprovementList = selfImprovementList,
                    techList = techList,
                    businessList = businessList,
                    foodList = foodList,
                    isLoading = false,
                )
            }
        } else {
            homeUiState = homeUiState.copy(isLoading = false, showError = true)
        }
    }
}
