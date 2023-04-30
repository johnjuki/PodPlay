package com.podplay.android.ui.screens.home

import androidx.lifecycle.ViewModel
import com.podplay.android.repository.PodcastRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val podcastRepo: PodcastRepo
) : ViewModel() {
}
