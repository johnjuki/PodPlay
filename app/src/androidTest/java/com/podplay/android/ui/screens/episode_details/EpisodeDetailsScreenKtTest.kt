package com.podplay.android.ui.screens.episode_details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.podplay.android.ui.theme.PodplayTheme
import com.podplay.android.util.Description
import org.junit.Rule
import org.junit.Test

class EpisodeDetailsScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun initialState_isRendered() {
        composeTestRule.setContent {
            PodplayTheme {
                EpisodeDetailsScreen(
                    navigateUp = {},
                    uiState = EpisodeDetailsUiState(isLoading = true),
                    onPlayPauseClick = {},
                    playPauseText = { "" }
                )
            }
        }
        composeTestRule.onNodeWithContentDescription(Description.LOADING).assertIsDisplayed()
    }
}
