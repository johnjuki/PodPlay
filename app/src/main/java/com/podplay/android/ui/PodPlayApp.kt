package com.podplay.android.ui

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.podplay.android.navigation.PodPlayNavHost
import com.podplay.android.navigation.TopLevelDestination
import com.podplay.android.navigation.homeNavRoute
import com.podplay.android.navigation.searchNavRoute
import com.podplay.android.navigation.subscriptionsNavRoute
import com.podplay.android.ui.screens.episode_player.PodPlayMinimizedPlayer
import com.podplay.android.ui.screens.episode_player.PodPlayPlayerScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodPlayApp(
    windowSizeClass: WindowSizeClass,
    appState: PodPlayAppState = rememberPodPlayAppState(windowSizeClass = windowSizeClass),
    backDispatcher: OnBackPressedDispatcher
) {
    Scaffold(
        bottomBar = {
            if (appState.shouldShowNavigationBar) {
                PodPlayNavBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                )
            }
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (appState.shouldShowNavRail) {
                PodPlayNavRail(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                )
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                PodPlayNavHost(navController = appState.navController)
                PodPlayMinimizedPlayer(
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
                PodPlayPlayerScreen(backDispatcher = backDispatcher)
            }
        }
    }
}

@Composable
private fun PodPlayNavRail(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    NavigationRail(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NavigationRailItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.selectedIconId),
                        contentDescription = stringResource(id = destination.labelId)
                    )
                },
                label = { Text(text = stringResource(id = destination.labelId)) }
            )
        }
    }
}

@Composable
private fun PodPlayNavBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    val routes = listOf(
        homeNavRoute,
        searchNavRoute,
        subscriptionsNavRoute,
    )
    val navBarDestination = routes.any { it == currentDestination?.route }
    AnimatedVisibility(
        visible = navBarDestination,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        NavigationBar(modifier) {
            destinations.forEach { destination ->
                val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigateToDestination(destination) },
                    icon = {
                        if (selected) {
                            Icon(
                                painter = painterResource(id = destination.selectedIconId),
                                contentDescription = stringResource(id = destination.labelId),
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = destination.unselectedIconId),
                                contentDescription = stringResource(id = destination.labelId),
                            )
                        }
                    },
                    label = { Text(text = stringResource(id = destination.labelId)) },
                )
            }
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
