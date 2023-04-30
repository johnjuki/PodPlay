package com.podplay.android.ui.screens.episode_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.podplay.android.data.model.Episode
import com.podplay.android.data.model.EpisodeViewData
import com.podplay.android.repository.PodcastRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(
    private val podcastRepo: PodcastRepo,
) : ViewModel() {

    var uiState by mutableStateOf(EpisodeDetailsUiState())

    fun getEpisode(guid: String) = viewModelScope.launch {
        uiState = uiState.copy(isLoading = true)
        podcastRepo.getEpisode(guid)?.let { episode ->
            uiState = uiState.copy(episode = episode, isLoading = false)
        } ?: run {
            uiState = uiState.copy(episode = Episode(), isLoading = false) // TODO: show Error instead
        }
    }

//    private fun getTitleAndImage(episode: Episode) = viewModelScope.launch {
//        val podcast = podcastRepo.getPodcastById(episode.podcastId!!)
//        uiState = uiState.copy(podcastName = podcast.feedTitle, podcastImageUrl = podcast.imageUrl)
//    }

    private fun episodeToEpisodeView(episode: Episode) : EpisodeViewData {
        return episode.let {
            val isVideo = it.mimeType.startsWith("video")
            EpisodeViewData(
                it.guid,
                it.title,
                it.description,
                it.mediaUrl,
                it.releaseDate,
                it.duration,
                isVideo
            )
        }
    }
}
