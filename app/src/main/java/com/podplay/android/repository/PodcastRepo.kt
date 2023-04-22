package com.podplay.android.repository

import androidx.lifecycle.LiveData
import com.podplay.android.model.Episode
import com.podplay.android.model.Podcast
import com.podplay.android.service.RssFeedResponse

interface PodcastRepo {

    suspend fun getPodcast(feedUrl: String): Podcast?

    fun rssItemsToEpisodes(episodeResponses: List<RssFeedResponse.EpisodeResponse>): List<Episode>

    fun rssResponseToPodcast(
        feedUrl: String, imageUrl: String, rssResponse: RssFeedResponse
    ): Podcast?

    fun savePodcast(podcast: Podcast)

    fun delete(podcast: Podcast)

    fun getAll(): LiveData<List<Podcast>>

    suspend fun getEpisode(guid: String) : Episode?

    suspend fun updatePodcastEpisodes(): MutableList<PodcastRepoImpl.PodcastUpdateInfo>

    suspend fun getNewEpisodes(localPodcast: Podcast): List<Episode>

    fun saveNewEpisodes(podcastId: Long, episodes: List<Episode>)
}
