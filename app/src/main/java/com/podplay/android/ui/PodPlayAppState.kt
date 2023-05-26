package com.podplay.android.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.podplay.android.navigation.TopLevelDestination
import com.podplay.android.navigation.homeNavRoute
import com.podplay.android.navigation.navigateHome
import com.podplay.android.navigation.navigateSearch
import com.podplay.android.navigation.navigateSubscriptions
import com.podplay.android.navigation.searchNavRoute
import com.podplay.android.navigation.subscriptionsNavRoute

@Composable
fun rememberPodPlayAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
): PodPlayAppState {
    return remember(navController, windowSizeClass) {
        PodPlayAppState(navController, windowSizeClass)
    }
}

class PodPlayAppState(
    val navController: NavHostController,
    private val windowSizeClass: WindowSizeClass,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            homeNavRoute -> TopLevelDestination.HOME
            searchNavRoute -> TopLevelDestination.SEARCH
            subscriptionsNavRoute -> TopLevelDestination.SUBSCRIPTIONS
            else -> null
        }

    val shouldShowNavigationBar : Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowNavigationBar

    val topLevelDestinations : List<TopLevelDestination> = TopLevelDestination.values().asList()

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateHome(topLevelNavOptions)
            TopLevelDestination.SEARCH -> navController.navigateSearch(topLevelNavOptions)
            TopLevelDestination.SUBSCRIPTIONS -> navController.navigateSubscriptions(topLevelNavOptions)
        }
    }
}
