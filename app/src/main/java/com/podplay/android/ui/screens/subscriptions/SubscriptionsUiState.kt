package com.podplay.android.ui.screens.subscriptions

import com.podplay.android.data.model.Podcast

sealed interface SubscriptionsUiState {
    object Loading: SubscriptionsUiState
    data class Success(val subscriptions: List<Podcast>) : SubscriptionsUiState
}
