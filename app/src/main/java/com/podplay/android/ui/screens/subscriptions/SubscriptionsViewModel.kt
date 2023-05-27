package com.podplay.android.ui.screens.subscriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.podplay.android.repository.PodcastRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    podcastRepo: PodcastRepo,
) : ViewModel() {
    val uiState : StateFlow<SubscriptionsUiState> =
        podcastRepo.loadSubscriptions()
            .map { SubscriptionsUiState.Success(it) }
            .stateIn(
                viewModelScope,
                initialValue = SubscriptionsUiState.Loading,
                started = SharingStarted.WhileSubscribed(5_000)
            )
}