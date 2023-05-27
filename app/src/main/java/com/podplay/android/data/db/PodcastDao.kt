package com.podplay.android.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.podplay.android.data.model.Episode
import com.podplay.android.data.model.Podcast
import kotlinx.coroutines.flow.Flow

@Dao
interface PodcastDao {
    @Query("SELECT * FROM Podcast ORDER BY FeedTitle")
    fun loadPodcasts(): LiveData<List<Podcast>>

    @Query("SELECT * FROM Podcast ORDER BY FeedTitle")
    suspend fun loadPodcastsStatic(): List<Podcast>

    @Query("SELECT * FROM Episode WHERE podcastId = :podcastId ORDER BY releaseDate DESC")
    suspend fun loadEpisodes(podcastId: Long): List<Episode>

    @Query("SELECT * FROM Podcast WHERE feedUrl = :url")
    suspend fun loadPodcast(url: String): Podcast?

    @Query("SELECT * FROM Podcast WHERE id = :id")
    suspend fun loadPodcastById(id: Long) : Podcast

    @Query("SELECT * FROM Episode WHERE guid = :guid")
    suspend fun loadEpisode(guid: String) : Episode?

    @Insert(onConflict = REPLACE)
    suspend fun insertPodcast(podcast: Podcast): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertEpisode(episode: Episode): Long

    @Query("SELECT * FROM Podcast WHERE isSubscribed = 1 ORDER BY id DESC")
    fun loadSubscriptions() : Flow<List<Podcast>>
}
