package com.podplay.android.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Podcast(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var feedUrl: String = "",
    var feedTitle: String = "",
    var feedDesc: String = "",
    var imageUrl: String = "",
    var lastUpdated: Date = Date(),
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

data class EpisodeViewData(
    var guid: String? = "",
    var title: String? = "",
    var description: String? = "",
    var mediaUrl: String? = "",
    var releaseDate: Date? = null,
    var duration: String? = "",
    var isVideo: Boolean = false,
)
