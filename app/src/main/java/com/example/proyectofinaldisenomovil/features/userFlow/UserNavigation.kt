package com.example.proyectofinaldisenomovil.features.userFlow

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.navigation.UserRoutes
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.features.LikedEvents.SavedEventsScreen
import com.example.proyectofinaldisenomovil.features.userFlow.CreateEvent.CreateEventScreen
import com.example.proyectofinaldisenomovil.features.userFlow.LikedEvents.LikedEventsScreen
import com.example.proyectofinaldisenomovil.features.userFlow.Notifications.NotificationsScreen
import com.example.proyectofinaldisenomovil.features.userFlow.Profile.ProfileScreen
import com.example.proyectofinaldisenomovil.features.userFlow.home.HomeScreen


@Composable
fun UserNavigation(
    onLogout: () -> Unit
) {
    val userNavController = rememberNavController()
    val currentEntry by userNavController.currentBackStackEntryAsState()

    // Mapea el destino actual al string que espera AppBottomBar
    val selectedRoute = when {
        currentEntry?.destination?.hasRoute(UserRoutes.Home::class) == true -> "home"
        currentEntry?.destination?.hasRoute(UserRoutes.CreateEvent::class) == true -> "createEvent"
        currentEntry?.destination?.hasRoute(UserRoutes.SavedEvents::class) == true -> "savedEvents"
        currentEntry?.destination?.hasRoute(UserRoutes.LikedEvents::class) == true -> "likedEvents"
        currentEntry?.destination?.hasRoute(UserRoutes.Profile::class) == true -> "profile"
        else -> ""
    }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                selectedRoute = selectedRoute,
                onHomeClick = {
                    userNavController.navigate(UserRoutes.Home) {
                        popUpTo(userNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onCreateEvent = {
                    userNavController.navigate(UserRoutes.CreateEvent) {
                        popUpTo(userNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSavedEvents = {
                    userNavController.navigate(UserRoutes.SavedEvents) {
                        popUpTo(userNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onLikedEvents = {
                    userNavController.navigate(UserRoutes.LikedEvents) {
                        popUpTo(userNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onProfile = {
                    userNavController.navigate(UserRoutes.Profile) {
                        popUpTo(userNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = userNavController,
            startDestination = UserRoutes.Home
        ) {
            composable<UserRoutes.Home> {
                HomeScreen(
                    paddingValues = paddingValues,
                    onNotificationClick = {
                        userNavController.navigate(UserRoutes.Notifications)
                    }
                )
            }

            composable<UserRoutes.CreateEvent> {
                CreateEventScreen(paddingValues = paddingValues)
            }

            composable<UserRoutes.SavedEvents> {
                SavedEventsScreen(paddingValues = paddingValues)
            }

            composable<UserRoutes.LikedEvents> {
                LikedEventsScreen(paddingValues = paddingValues)
            }

            composable<UserRoutes.Profile> {
                ProfileScreen(
                    paddingValues = paddingValues,
                    onLogout = onLogout
                )
            }

            composable<UserRoutes.Notifications> {
                NotificationsScreen(
                    //TODO eliminar el navController
                    navController = userNavController
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserNavigationPreview(){
    ProyectoFinalDisenoMovilTheme() {
        UserNavigation(onLogout = {})
    }
}
