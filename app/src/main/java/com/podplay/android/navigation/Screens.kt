package com.podplay.android.navigation

import com.podplay.android.util.Constants.FEED_URL_KEY
import com.podplay.android.util.Constants.GUID_KEY

sealed class Screens(val route: String) {

    object Home : Screens(route = "home_route")

    object Search : Screens(route = "search_route")

    object PodcastDetails : Screens(route = "podcast_route/{$FEED_URL_KEY}") {
        fun replaceFeedUrl(feedUrl: String): String {
            return this.route.replace("{$FEED_URL_KEY}", feedUrl)
        }
    }

    object EpisodeDetails :
        Screens(route = "episode_route/{$GUID_KEY}") {
        fun replaceGuid(guid: String): String {
            return this.route.replace("{$GUID_KEY}", guid)
        }
    }
}
