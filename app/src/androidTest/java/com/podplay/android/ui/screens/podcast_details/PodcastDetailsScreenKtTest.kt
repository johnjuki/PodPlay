package com.podplay.android.ui.screens.podcast_details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
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
                    navigateUp = { /*TODO*/ },
                    onEpisodeClick = {}
                )
            }
        }
        composeTestRule.onNodeWithContentDescription(Description.PODCAST_DETAILS_LOADING).assertIsDisplayed()
    }
}
