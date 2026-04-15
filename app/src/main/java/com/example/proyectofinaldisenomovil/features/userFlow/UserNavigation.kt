package com.example.proyectofinaldisenomovil.features.userFlow

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.features.LikedEvents.SavedEventsScreen
import com.example.proyectofinaldisenomovil.features.userFlow.CreateEvent.CreateEventScreen
import com.example.proyectofinaldisenomovil.features.userFlow.LikedEvents.LikedEventsScreen
import com.example.proyectofinaldisenomovil.features.userFlow.Notifications.NotificationsScreen
import com.example.proyectofinaldisenomovil.features.userFlow.Profile.ProfileScreen
import com.example.proyectofinaldisenomovil.features.userFlow.ViewEvent.ViewEventScreen
import com.example.proyectofinaldisenomovil.features.userFlow.home.HomeScreen

object UserRoutes {
    const val HOME = "home"
    const val SAVED_EVENTS = "savedEvents"
    const val CREATE_EVENT = "createEvent"
    const val LIKED_EVENTS = "likedEvents"
    const val PROFILE = "profile"
    const val NOTIFICATIONS = "notifications"
    const val EVENT_DETAIL = "eventDetail"
}

@Composable
fun UserNavigation(
    onLogout: () -> Unit
) {
    val userNavController = rememberNavController()
    val currentEntry by userNavController.currentBackStackEntryAsState()

    val selectedRoute = currentEntry?.destination?.route ?: UserRoutes.HOME

    Scaffold(
        bottomBar = {
            AppBottomBar(
                selectedRoute = selectedRoute,
                onHomeClick = {
                    userNavController.navigate(UserRoutes.HOME) {
                        popUpTo(UserRoutes.HOME) { inclusive = true }
                    }
                },
                onCreateEvent = {
                    userNavController.navigate(UserRoutes.CREATE_EVENT) {
                        popUpTo(UserRoutes.HOME)
                    }
                },
                onSavedEvents = {
                    userNavController.navigate(UserRoutes.SAVED_EVENTS) {
                        popUpTo(UserRoutes.HOME)
                    }
                },
                onLikedEvents = {
                    userNavController.navigate(UserRoutes.LIKED_EVENTS) {
                        popUpTo(UserRoutes.HOME)
                    }
                },
                onProfile = {
                    userNavController.navigate(UserRoutes.PROFILE) {
                        popUpTo(UserRoutes.HOME)
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = userNavController,
            startDestination = UserRoutes.HOME
        ) {
            composable(UserRoutes.HOME) {
                HomeScreen(
                    paddingValues = paddingValues,
                    onNotificationClick = {
                        userNavController.navigate(UserRoutes.NOTIFICATIONS)
                    },
                    onEventClick = { eventId ->
                        userNavController.navigate("${UserRoutes.EVENT_DETAIL}/$eventId")
                    }
                )
            }

            composable(UserRoutes.CREATE_EVENT) {
                CreateEventScreen(
                    paddingValues = paddingValues,
                    onBackClick = {
                        userNavController.popBackStack()
                    },
                    onNotificationClick = {
                        userNavController.navigate(UserRoutes.NOTIFICATIONS)
                    },
                    onEventCreated = {
                        userNavController.popBackStack()
                    }
                )
            }

            composable(UserRoutes.SAVED_EVENTS) {
                SavedEventsScreen(
                    paddingValues = paddingValues,
                    onNotificationClick = {
                        userNavController.navigate(UserRoutes.NOTIFICATIONS)
                    }
                )
            }

            composable(UserRoutes.LIKED_EVENTS) {
                LikedEventsScreen(
                    paddingValues = paddingValues,
                    onNotificationClick = {
                        userNavController.navigate(UserRoutes.NOTIFICATIONS)
                    }
                )
            }

            composable(UserRoutes.PROFILE) {
                ProfileScreen(
                    paddingValues = paddingValues,
                    onLogout = onLogout,
                    onNotificationClick = {
                        userNavController.navigate(UserRoutes.NOTIFICATIONS)
                    },
                    onBackClick = {
                        userNavController.popBackStack()
                    }
                )
            }

            composable(UserRoutes.NOTIFICATIONS) {
                NotificationsScreen(
                    paddingValues = paddingValues,
                    onBackClick = { userNavController.popBackStack() }
                )
            }

            composable(
                route = "${UserRoutes.EVENT_DETAIL}/{eventId}",
                arguments = listOf(navArgument("eventId") { type = NavType.StringType })) { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                // Aquí llamas a tu pantalla de detalle (ej: EventDetailScreen)
                ViewEventScreen(
                    eventId = eventId,
                    onBackClick = { userNavController.popBackStack() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserNavigationPreview() {
    ProyectoFinalDisenoMovilTheme {
        UserNavigation(onLogout = {})
    }
}
