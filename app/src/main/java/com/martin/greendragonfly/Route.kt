package com.martin.greendragonfly
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object RouteToday : NavKey

@Serializable
data class RouteList(val id: Int) : NavKey

@Serializable
data object RouteSetting: NavKey


enum class RouteDestinations(
    val label: String,
    val icon: Int,
    val route: NavKey
) {
    TODAY("Today", R.drawable.today_24, RouteToday),
    LIST("List", R.drawable.list_24, RouteList(1)),
    SETTING("Settings", R.drawable.settings_24, RouteSetting),
}