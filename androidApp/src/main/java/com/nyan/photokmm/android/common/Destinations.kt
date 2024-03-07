package com.nyan.photokmm.android.common

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

val photoDestinations = listOf(Dashboard)