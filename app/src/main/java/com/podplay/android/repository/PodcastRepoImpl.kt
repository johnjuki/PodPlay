package com.podplay.android.repository

import androidx.lifecycle.LiveData
import com.podplay.android.data.db.PodcastDao
import com.podplay.android.data.model.Episode
import com.podplay.android.data.model.Podcast
import com.podplay.android.data.service.RssFeedResponse
import com.podplay.android.data.service.RssFeedService
import com.podplay.android.util.DateUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
class PodcastRepoImpl @Inject constructor(
    private var podcastDao: PodcastDao,
) : PodcastRepo {

    private val feedService = RssFeedService.instance

    override suspend fun getPodcast(feedUrl: String): Podcast? {
        val podcastLocal = podcastDao.loadPodcast(feedUrl)
        if (podcastLocal != null) {
            podcastLocal.id?.let {
                podcastLocal.episodes = podcastDao.loadEpisodes(it)
                return podcastLocal
            }
        }
        var podcast: Podcast? = null
        val feedResponse = feedService.getFeed(feedUrl)
        if (feedResponse != null) {
            podcast = rssResponseToPodcast(feedUrl, "", feedResponse)
        }
        return podcast
    }

    override fun rssItemsToEpisodes(episodeResponses: List<RssFeedResponse.EpisodeResponse>): List<Episode> {
        return episodeResponses.map {
            Episode(
                it.guid ?: "",
                null,
                null,
                null,
                it.title ?: "",
                it.description ?: "",
                it.url ?: "",
                it.type ?: "",
                DateUtils.xmlDateToDate(it.pubDate),
                it.duration ?: ""
            )
        }
    }

    override fun rssResponseToPodcast(
        feedUrl: String, imageUrl: String, rssResponse: RssFeedResponse
    ): Podcast? {
        val items = rssResponse.episodes ?: return null
        val description = if (rssResponse.description == "")
            rssResponse.summary else rssResponse.description
        return Podcast(
            null, feedUrl, rssResponse.title, description, imageUrl,
            rssResponse.lastUpdated, episodes = rssItemsToEpisodes(items)
        )
    }

    override fun savePodcast(podcast: Podcast) {
        GlobalScope.launch {
            val podcastId = podcastDao.insertPodcast(podcast)
            for (episode in podcast.episodes) {
                episode.podcastId = podcastId
                episode.imageUrl = podcast.imageUrl
                episode.podcastName = podcast.feedTitle
                podcastDao.insertEpisode(episode)
            }
        }
    }

    override fun delete(podcast: Podcast) {
        GlobalScope.launch {
            podcastDao.deletePodcast(podcast)
        }
    }

    override fun getAll(): LiveData<List<Podcast>> {
        return podcastDao.loadPodcasts()
    }

    override suspend fun getPodcastById(id: Long): Podcast {
        return podcastDao.loadPodcastById(id)
    }

    override suspend fun getEpisode(guid: String): Episode? {
        return podcastDao.loadEpisode(guid)
    }

    override suspend fun updatePodcastEpisodes(): MutableList<PodcastUpdateInfo> {
        val updatedPodcasts: MutableList<PodcastUpdateInfo> = mutableListOf()
        val podcasts = podcastDao.loadPodcastsStatic()
        for (podcast in podcasts) {
            val newEpisodes = getNewEpisodes(podcast)
            if (newEpisodes.isNotEmpty()) {
                podcast.id?.let {
                    saveNewEpisodes(it, newEpisodes)
                    updatedPodcasts.add(
                        PodcastUpdateInfo(
                            podcast.feedUrl,
                            podcast.feedTitle,
                            newEpisodes.count()
                        )
                    )
                }
            }
        }
        return updatedPodcasts
    }

    override suspend fun getNewEpisodes(localPodcast: Podcast): List<Episode> {
        val response = feedService.getFeed(localPodcast.feedUrl)
        if (response != null) {
            val remotePodcast =
                rssResponseToPodcast(localPodcast.feedUrl, localPodcast.imageUrl, response)
            remotePodcast?.let {
                val localEpisodes = podcastDao.loadEpisodes(localPodcast.id!!)
                return remotePodcast.episodes.filter { episode ->
                    localEpisodes.find { episode.guid == it.guid } == null
                }
            }
        }
        return listOf()
    }

    override fun saveNewEpisodes(podcastId: Long, episodes: List<Episode>) {
        GlobalScope.launch {
            for (episode in episodes) {
                episode.podcastId = podcastId
                podcastDao.insertEpisode(episode)
            }
        }
    }

    class PodcastUpdateInfo(val feedUrl: String, val name: String, val newCount: Int)
}
