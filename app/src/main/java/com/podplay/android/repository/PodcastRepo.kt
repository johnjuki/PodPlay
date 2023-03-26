package com.podplay.android.repository

import com.podplay.android.model.Episode
import com.podplay.android.model.Podcast
import com.podplay.android.service.RssFeedResponse
import com.podplay.android.service.RssFeedService
import com.podplay.android.util.DateUtils

class PodcastRepo(private val feedService: RssFeedService) {

    fun getPodcast(feedUrl: String) : Podcast? {
        var podcast: Podcast? = null
        val feedResponse = feedService.getFeed(feedUrl)
        if (feedResponse != null) {
            podcast = rssResponseToPodcast(feedUrl, "", feedResponse)
        }
        return podcast
    }

    private fun rssItemsToEpisodes(
        episodeResponses: List<RssFeedResponse.EpisodeResponse>
    ): List<Episode> {
        return episodeResponses.map {
            Episode(
                it.guid ?: "",
                it.title ?: "",
                it.description ?: "",
                it.url ?: "",
                it.type ?: "",
                DateUtils.xmlDateToDate(it.pubDate),
                it.duration ?: ""
            )
        }
    }

    private fun rssResponseToPodcast(
        feedUrl: String, imageUrl: String, rssResponse:
        RssFeedResponse
    ): Podcast? {
        val items = rssResponse.episodes ?: return null
        val description = if (rssResponse.description == "")
            rssResponse.summary else rssResponse.description
        return Podcast(feedUrl, rssResponse.title, description,
            imageUrl,
            rssResponse.lastUpdated, episodes =
            rssItemsToEpisodes(items))
    }
}
