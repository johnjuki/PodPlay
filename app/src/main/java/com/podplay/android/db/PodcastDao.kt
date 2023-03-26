package com.podplay.android.db

import com.podplay.android.model.Episode
import com.podplay.android.model.Podcast
import java.nio.charset.CodingErrorAction.REPLACE

@Dao
interface PodcastDao {
    @Query("SELECT * FROM Podcast ORDER BY FeedTitle")
    fun loadPodcasts() : LiveData<List<Podcast>>

    @Query("SELECT * FROM Episode WHERE podcastId = :podcastId ORDER BY releaseDate DESC")
    suspend fun loadEpisodes(podcastId: Long) : List<Episode>

    @Query("SELECT * FROM Podcast WHERE feedUrl = :url")
    fun loadPodcast(url: String) : Podcast?

    @Insert(onConflict = REPLACE)
    suspend fun insertPodcast(podcast: Podcast) : Long

    @Insert(OnConflict = REPLACE)
    suspend fun insertEpisode(episode: Episode) : Long

    @Delete
    fun deletePodcast(podcast: Podcast)

    @Query("SELECT * FROM Podcast ORDER BY FeedTitle")
    fun loadPodcastsStatic() : List<Podcast>
}