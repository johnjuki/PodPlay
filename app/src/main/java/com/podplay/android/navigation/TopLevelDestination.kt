package com.podplay.android.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.podplay.android.R

enum class TopLevelDestination(
    @DrawableRes val selectedIconId: Int,
    @DrawableRes val unselectedIconId: Int,
    @StringRes val labelId: Int,
) {
    HOME(
        selectedIconId = R.drawable.baseline_home_24,
        unselectedIconId = R.drawable.outline_home_24,
        labelId = R.string.home,
    ),
    SEARCH(
        selectedIconId = R.drawable.baseline_search_24,
        unselectedIconId = R.drawable.outline_search_24,
        labelId = R.string.search,
    ),
    SUBSCRIPTIONS(
        selectedIconId = R.drawable.baseline_check_circle_24,
        unselectedIconId = R.drawable.baseline_check_circle_outline_24,
        labelId = R.string.subscriptions
    ),
}

const val homeNavRoute = "home_route"
const val searchNavRoute = "search_route"
const val subscriptionsNavRoute = "subscriptions_route"

fun NavController.navigateHome(navOptions: NavOptions? = null) {
    this.navigate(homeNavRoute, navOptions)
}

fun NavController.navigateSearch(navOptions: NavOptions? = null) {
    this.navigate(searchNavRoute, navOptions)
}

fun NavController.navigateSubscriptions(navOptions: NavOptions? = null) {
    this.navigate(subscriptionsNavRoute, navOptions)
}
