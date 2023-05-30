package com.podplay.android.ui.screens.podcast_details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.podplay.android.R
import com.podplay.android.data.model.PodcastDummyData
import com.podplay.android.ui.theme.PodplayTheme
import com.podplay.android.util.Description
import org.junit.Rule
import org.junit.Test

class PodcastDetailsScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun initialState_isRendered() {
        composeTestRule.setContent {
            PodplayTheme {
                PodcastDetailsScreen(
                    uiState = PodcastDetailsUiState(isSearching = true),
                    navigateUp = {},
                    onEpisodeClick = {},
                    onSubscribeButtonClick = {}
                )
            }
        }
        composeTestRule.onNodeWithContentDescription(Description.LOADING).assertIsDisplayed()
    }

    @Test
    fun stateWithContent_isRendered() {
        val podcast = PodcastDummyData.podcast
        val latestEpisode = podcast.episodes[0]
        composeTestRule.setContent {
            PodplayTheme {
                PodcastDetailsScreen(
                    uiState = PodcastDetailsUiState(podcast),
                    navigateUp = {},
                    onSubscribeButtonClick = {},
                    onEpisodeClick = {}
                )
            }
        }
        composeTestRule.onNodeWithContentDescription(Description.PODCAST_IMAGE).assertIsDisplayed()
        composeTestRule.onNodeWithText(podcast.feedTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.episodes))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.description))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(latestEpisode.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(latestEpisode.description).assertIsDisplayed()
    }

    @Test
    fun stateWithContent_ClickOnEpisode_isRegistered() {
        val podcast = PodcastDummyData.podcast
        val latestEpisode = podcast.episodes[0]
        composeTestRule.setContent {
            PodplayTheme {
                PodcastDetailsScreen(
                    uiState = PodcastDetailsUiState(podcast = podcast),
                    navigateUp = {},
                    onSubscribeButtonClick = {},
                    onEpisodeClick = { guid ->
                        assert(guid == latestEpisode.guid)
                    }
                )
            }
        }
        composeTestRule.onNodeWithText(latestEpisode.title).performClick()
    }
}
