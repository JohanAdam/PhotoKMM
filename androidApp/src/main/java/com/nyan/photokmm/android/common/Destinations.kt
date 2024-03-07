package com.nyan.photokmm.android.common

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destinations {
    val title: String
    val route: String
    val routeWithArgs: String
}

object Dashboard: Destinations {
    override val title: String
        get() = "Photos"
    override val route: String
        get() = "dashboard"
    override val routeWithArgs: String
        get() = route

}

object Detail: Destinations {
    override val title: String
        get() = "Photo details"
    override val route: String
        get() = "detail"
    override val routeWithArgs: String
        get() = "$route/{photoId}"

    val arguments = listOf(
        navArgument(name = "photoId") {
            type = NavType.StringType
        }
    )
}

val photoDestinations = listOf(Dashboard, Detail)