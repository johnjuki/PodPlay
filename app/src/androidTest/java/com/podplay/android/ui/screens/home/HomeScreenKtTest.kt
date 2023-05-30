package com.podplay.android.ui.screens.home

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.podplay.android.R
import com.podplay.android.data.model.PodcastDummyData
import com.podplay.android.ui.theme.PodplayTheme
import com.podplay.android.util.Description
import org.junit.Rule
import org.junit.Test

class HomeScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun initialState_isRendered() {
        composeTestRule.setContent {
            PodplayTheme {
                HomeScreen(uiState = HomeUiState(), onPodcastClick = { _, _ -> })
            }
        }
        composeTestRule.onNodeWithContentDescription(Description.LOADING).assertIsDisplayed()
    }

    @Test
    fun stateWithContent_isRendered() {
        val healthList = PodcastDummyData.healthList
        val selfImprovementList = PodcastDummyData.selfImprovementList
        val techList = PodcastDummyData.techList
        val businessList = PodcastDummyData.businessList
        val foodList = PodcastDummyData.foodList
        composeTestRule.setContent {
            PodplayTheme {
                HomeScreen(
                    uiState = HomeUiState(
                        healthList = healthList,
                        selfImprovementList = selfImprovementList,
                        techList = techList,
                        businessList = businessList,
                        foodList = foodList,
                        isLoading = false,
                    ),
                    onPodcastClick = { _, _ -> }
                )
            }
        }
        assertNodeWithText(R.string.health)
        assertNodeWithText(R.string.self_improvement)
        assertNodeWithText(R.string.technology)
        assertNodeWithText(R.string.business)
        assertNodeWithText(R.string.food)
        composeTestRule.onAllNodesWithContentDescription(Description.PODCAST_IMAGE)
            .assertCountEquals(healthList.size + selfImprovementList.size + techList.size + businessList.size + foodList.size)

    }

    @Test
    fun stateWithContent_ClickOnPodcastImage_isRegistered() {
        val healthList = PodcastDummyData.healthList
        val selfImprovementList = PodcastDummyData.selfImprovementList
        val techList = PodcastDummyData.techList
        val businessList = PodcastDummyData.businessList
        val foodList = PodcastDummyData.foodList
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomeUiState(
                    healthList = healthList,
                    selfImprovementList = selfImprovementList,
                    techList = techList,
                    businessList = businessList,
                    foodList = foodList,
                    isLoading = false,
                ),
                onPodcastClick = { feedUrl, imageUrl -> assert(feedUrl == healthList[0].feedUrl) }
            )
        }
        composeTestRule.onAllNodesWithContentDescription(Description.PODCAST_IMAGE).onFirst().performClick()
    }

    private fun assertNodeWithText(@StringRes string: Int) {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(string))
            .assertIsDisplayed()
    }
}