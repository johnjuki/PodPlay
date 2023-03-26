package com.podplay.android.repository

import com.podplay.android.model.Podcast

class PodcastRepo {
    fun getPodcast(feedUrl: String) : Podcast? {
        return Podcast(feedUrl, "No Name", "No description", "No image")
    }
}