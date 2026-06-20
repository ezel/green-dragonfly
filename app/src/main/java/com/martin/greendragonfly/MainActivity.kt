package com.martin.greendragonfly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.martin.greendragonfly.data.HomeworkRepository
import com.martin.greendragonfly.data.UserPreferencesRepository
import com.martin.greendragonfly.data.dataStore
import com.martin.greendragonfly.ui.login.LoginScreen
import com.martin.greendragonfly.ui.login.LoginViewModel.Companion.provideFactory
import com.martin.greendragonfly.ui.theme.GreenDragonflyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GreenDragonflyTheme {
                GreenDragonApp(dataStore = this.dataStore)
            }
        }
    }
}


@Composable
fun GreenDragonApp(dataStore: DataStore<Preferences> /*db: HomeWorkDatabase*/) {
    var currentDestination by rememberSaveable { mutableStateOf(RouteDestinations.TODAY) }
    val backStack = remember { mutableStateListOf<Any>(RouteToday) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            RouteDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            painterResource(it.icon),
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = {
                        if (backStack.last() !== it.route) {
                            backStack.add(it.route);
                            currentDestination = it
                        }
                    }
                )
            }
        }
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<RouteToday> {
                    currentDestination = RouteDestinations.TODAY
                    //HomeScreen()
                }
                entry<RouteList> {
                    currentDestination = RouteDestinations.LIST

                }
                entry<RouteSetting> {
                    currentDestination = RouteDestinations.SETTING
                    val repo = UserPreferencesRepository(dataStore)
                    LoginScreen(viewModel(factory = provideFactory(repo, HomeworkRepository())))
                }
            }
        )
    }
}