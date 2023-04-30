package com.podplay.android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.podplay.android.data.model.Episode
import com.podplay.android.data.model.EpisodeViewData
import com.podplay.android.data.model.Podcast
import com.podplay.android.data.model.PodcastViewData
import com.podplay.android.repository.PodcastRepo
import com.podplay.android.ui.screens.search.SearchViewModel
import com.podplay.android.util.DateUtils

//class PodcastViewModel(application: Application) : AndroidViewModel(application) {
//
//    var podcastRepo: PodcastRepo? = null
//    private val _podcastLiveData = MutableLiveData<PodcastViewData?>()
//    val podcastLiveData: LiveData<PodcastViewData?> = _podcastLiveData
//    var livePodcastSummaryData: LiveData<List<SearchViewModel.PodcastSummaryViewData>>? = null
//    var activeEpisodeViewData: EpisodeViewData? = null
//
////    val podcastDao: PodcastDao = PodPlayDatabase
////        .getInstance(application, viewModelScope)
////        .podcastDao()
//
//    private var activePodcast: Podcast? = null
//
//    suspend fun setActivePodcast(feedUrl: String): SearchViewModel.PodcastSummaryViewData? {
//        val repo = podcastRepo ?: return null
//        val podcast = repo.getPodcast(feedUrl)
//        return if (podcast == null) {
//            null
//        } else {
//            _podcastLiveData.value = podcastToPodcastView(podcast)
//            activePodcast = podcast
//            podcastToSummaryView(podcast)
//        }
//    }
//
//    suspend fun getPodcast(podcastSummaryViewData: SearchViewModel.PodcastSummaryViewData) {
//        podcastSummaryViewData.feedUrl?.let { url ->
//            podcastRepo?.getPodcast(url)?.let {
//                it.feedTitle = podcastSummaryViewData.name ?: ""
//                it.imageUrl = podcastSummaryViewData.imageUrl ?: ""
//                _podcastLiveData.value = podcastToPodcastView(it)
//                activePodcast = it
//            } ?: run {
//                _podcastLiveData.value = null
//            }
//        } ?: run {
//            _podcastLiveData.value = null
//        }
//    }
//
//    fun getPodcasts(): LiveData<List<SearchViewModel.PodcastSummaryViewData>>? {
//        val repo = podcastRepo ?: return null
//        if (livePodcastSummaryData == null) {
//            val liveData = repo.getAll()
//            livePodcastSummaryData = liveData.map { podcastList ->
//                podcastList.map { podcast ->
//                    podcastToSummaryView(podcast)
//                }
//            }
//        }
//
//        return livePodcastSummaryData
//    }
//
//    fun saveActivePodcast() {
//        val repo = podcastRepo ?: return
//        activePodcast?.let {
//            repo.savePodcast(it)
//        }
//    }
//
//    private fun podcastToPodcastView(podcast: Podcast): PodcastViewData {
//        return PodcastViewData(
//            podcast.id != null,
//            podcast.feedTitle,
//            podcast.feedUrl,
//            podcast.feedDesc,
//            podcast.imageUrl,
//            episodesToEpisodesView(podcast.episodes)
//        )
//    }
//
//    private fun podcastToSummaryView(podcast: Podcast):
//            SearchViewModel.PodcastSummaryViewData {
//        return SearchViewModel.PodcastSummaryViewData(
//            podcast.feedTitle,
//            DateUtils.dateToShortDate(podcast.lastUpdated),
//            podcast.imageUrl,
//            podcast.feedUrl
//        )
//    }
//
//    private fun episodesToEpisodesView(episodes: List<Episode>): List<EpisodeViewData> {
//        return episodes.map {
//            val isVideo = it.mimeType.startsWith("video")
//            EpisodeViewData(
//                it.guid,
//                it.title,
//                it.description,
//                it.mediaUrl,
//                it.releaseDate,
//                it.duration,
//                isVideo
//            )
//        }
//    }
//
//    fun deleteActivePodcast() {
//        val repo = podcastRepo ?: return
//        activePodcast?.let {
//            repo.delete(it)
//        }
//    }
//}
