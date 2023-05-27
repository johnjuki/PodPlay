package com.podplay.android.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Podcast(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var feedUrl: String = "",
    var feedTitle: String = "",
    var feedDesc: String = "",
    var imageUrl: String = "",
    var lastUpdated: Date = Date(),
    var isSubscribed: Boolean = false,
    @Ignore var episodes: List<Episode> = listOf(),
)

data class PodcastViewData(
    var subscribed: Boolean = false,
    var feedTitle: String? = "",
    var feedUrl: String? = "",
    var feedDesc: String? = "",
    var imageUrl: String? = "",
    var episodes: List<EpisodeViewData>,
)

// Dummy Data
object PodcastDummyData {
    val podcast = Podcast(
        id = 1,
        feedUrl = "url",
        feedTitle = "All In The Mind",
        feedDesc = "Description",
        imageUrl = "image.url",
        lastUpdated = Date(System.currentTimeMillis()),
        episodes = listOf(
            Episode(
                guid = "1",
                podcastId = 1,
                title = "How to Change For Good",
                description = "episode description",
                mediaUrl = "url",
                mimeType = "audio",
                releaseDate = Date(System.currentTimeMillis()),
                duration = "32:23"
            )
        )
    )
}
