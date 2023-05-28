package com.podplay.android.ui.screens.podcast_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.podplay.android.data.model.Podcast
import com.podplay.android.repository.ItunesRepo
import com.podplay.android.repository.PodcastRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastDetailsViewModel @Inject constructor(
    private val podcastRepo: PodcastRepo,
    private val itunesRepo: ItunesRepo,
) : ViewModel() {

    var uiState by mutableStateOf(PodcastDetailsUiState())

    fun updateImageUrl(imageUrl: String) {
        uiState = uiState.copy(imageUrl = imageUrl)
    }

    fun getPodcast(feedUrl: String) = viewModelScope.launch {
        uiState = uiState.copy(isSearching = true)
        podcastRepo.getPodcast(feedUrl)?.let {
            var podcast = it
            podcast = podcast.copy(imageUrl = uiState.imageUrl)
            podcastRepo.savePodcast(podcast)
            uiState = uiState.copy(podcast = podcast, isSearching = false)
        } ?: run {
            uiState =
                uiState.copy(podcast = Podcast(), isSearching = false) // TODO: Show Error instead
        }
    }

    fun subscribe() = viewModelScope.launch {
        uiState =
            uiState.copy(podcast = uiState.podcast.copy(isSubscribed = !uiState.podcast.isSubscribed))
           podcastRepo.updateSubscription(uiState.podcast)
    }
}
