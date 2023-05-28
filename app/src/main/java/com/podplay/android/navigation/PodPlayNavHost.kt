package com.podplay.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.podplay.android.ui.screens.episode_details.EpisodeDetailsRoute
import com.podplay.android.ui.screens.home.HomeRoute
import com.podplay.android.ui.screens.podcast_details.PodcastDetailsRoute
import com.podplay.android.ui.screens.search.SearchRoute
import com.podplay.android.ui.screens.subscriptions.SubscriptionsRoute
import com.podplay.android.util.Constants.FEED_URL_KEY
import com.podplay.android.util.Constants.GUID_KEY
import com.podplay.android.util.Constants.IMAGE_URL_KEY
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun PodPlayNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = homeNavRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {

        // HOME
        composable(homeNavRoute) {
            HomeRoute(
                onPodcastClick = { feedUrl, imageUrl ->
                    navigateToPodcastDetails(feedUrl, imageUrl, navController)
                }
            )
        }

        // Search
        composable(searchNavRoute) {
            SearchRoute(
                onPodcastClick = { feedUrl, imageUrl ->
                    navigateToPodcastDetails(feedUrl, imageUrl, navController)
                },
            )
        }

        // SUBSCRIPTIONS
        composable(subscriptionsNavRoute) {
            SubscriptionsRoute(
                onPodcastClick = { feedUrl, imageUrl ->
                    navigateToPodcastDetails(feedUrl, imageUrl, navController)
                }
            )
        }

        // PODCAST DETAILS
        composable(
            route = Screens.PodcastDetails.route,
            arguments = listOf(
                navArgument(FEED_URL_KEY) { type = NavType.StringType },
                navArgument(IMAGE_URL_KEY) { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val feedUrl = navBackStackEntry.arguments?.getString(FEED_URL_KEY) ?: ""
            val imageUrl = navBackStackEntry.arguments?.getString(IMAGE_URL_KEY) ?: ""
            PodcastDetailsRoute(
                feedUrl = feedUrl,
                imageUrl = imageUrl,
                navigateUp = { navController.navigateUp() },
                onEpisodeClick = { guid ->
                    val encodedGuid = URLEncoder.encode(guid, StandardCharsets.UTF_8.toString())
                    navController.navigate(
                        Screens.EpisodeDetails.replaceGuid(encodedGuid)
                    )
                }
            )
        }

        // EPISODE DETAILS
        composable(
            route = Screens.EpisodeDetails.route,
            arguments = listOf(
                navArgument(GUID_KEY) { type = NavType.StringType },
            )
        ) { navBackStackEntry ->
            val guid = navBackStackEntry.arguments?.getString(GUID_KEY) ?: ""
            EpisodeDetailsRoute(
                guid = guid,
                navigateUp = { navController.navigateUp() }
            )
        }

    }
}

private fun navigateToPodcastDetails(
    feedUrl: String,
    imageUrl: String,
    navController: NavHostController
) {
    val encodedFeedUrl =
        URLEncoder.encode(feedUrl, StandardCharsets.UTF_8.toString())
    val encodedImageUrl =
        URLEncoder.encode(imageUrl, StandardCharsets.UTF_8.toString())
    navController.navigate(
        Screens.PodcastDetails.replaceRoute(
            encodedFeedUrl,
            encodedImageUrl
        )
    )
}
