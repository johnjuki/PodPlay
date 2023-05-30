package com.podplay.android.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.podplay.android.data.service.PodcastResponse
import com.podplay.android.util.DateUtils
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

fun itunesPodcastToPodcastSummaryView(
    itunesPodcast: PodcastResponse.ItunesPodcast):
        PodcastSummaryViewData {
    return PodcastSummaryViewData(
        itunesPodcast.collectionCensoredName,
        DateUtils.jsonDateToShortDate(itunesPodcast.releaseDate),
        itunesPodcast.artworkUrl100,
        itunesPodcast.feedUrl)
}

data class PodcastSummaryViewData(
    var name: String? = "",
    var lastUpdated: String? = "",
    var imageUrl: String? = "",
    var feedUrl: String? = ""
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
            ),
            Episode(
                guid = "2",
                podcastId = 2,
                title = "Self-discipline",
                description = " Self-discipline episode description",
                mediaUrl = "url",
                mimeType = "audio",
                releaseDate = Date(System.currentTimeMillis()),
                duration = "32:23"
            ),
        )
    )

    val healthList = listOf(
        PodcastSummaryViewData(
            name = "On Purpose",
            imageUrl = "image.url",
            feedUrl = "feed.url",
        ),
        PodcastSummaryViewData(
            name = "Doctors Farmacy",
            imageUrl = "image.url",
            feedUrl = "feed.url",
        ),
    )
    val selfImprovementList = listOf(
        PodcastSummaryViewData(
            name = "Learn This Sooner",
            imageUrl = "image.url",
            feedUrl = "feed.url",
        ),
        PodcastSummaryViewData(
            name = "The Hardcore Self Help Podcast",
            imageUrl = "image.url",
            feedUrl = "feed.url",
        ),
    )
    val techList = listOf(
        PodcastSummaryViewData(
            name = "Now In Android",
            imageUrl = "image.url",
            feedUrl = "feed.url",
        ),
        PodcastSummaryViewData(
            name = "Android Developers Backstage",
            imageUrl = "image.url",
            feedUrl = "feed.url",
        )
    )
    val businessList = listOf(
        PodcastSummaryViewData(
            name = "The Journal",
            imageUrl = "image.url",
            feedUrl = "feed.url",
        ),
        PodcastSummaryViewData(
            name = "Rich Dad Show",
            imageUrl = "image.url",
            feedUrl = "feed.url",
        ),
    )
    val foodList = listOf(
        PodcastSummaryViewData(
            name = "Gastro pod",
            imageUrl = "image.url",
            feedUrl = "feed.url",
        ),
        PodcastSummaryViewData(
            name = "The Splendid..",
            imageUrl = "image.url",
            feedUrl = "feed.url",
        ),
    )
}
