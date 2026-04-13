package com.example.proyectofinaldisenomovil.features.moderatorFlow

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.features.moderatorFlow.eventDetail.ModeratorEventDetailScreen
import com.example.proyectofinaldisenomovil.features.moderatorFlow.panel.ModeratorPanelScreen

object ModeratorRoutes {
    const val PANEL = "moderator_panel"
    const val EVENT_DETAIL = "moderator_event_detail"
}

@Composable
fun ModeratorNavigation(
    onLogout: () -> Unit,
    paddingValues: PaddingValues = PaddingValues(),
) {
    val moderatorNavController = rememberNavController()
    val navBackStackEntry by moderatorNavController.currentBackStackEntryAsState()

    NavHost(
        navController = moderatorNavController,
        startDestination = ModeratorRoutes.PANEL,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(ModeratorRoutes.PANEL) {
            ModeratorPanelScreen(
                onEventClick = { eventId ->
                    moderatorNavController.navigate("${ModeratorRoutes.EVENT_DETAIL}/$eventId")
                },
                onLogout = onLogout,
            )
        }

        composable(
            route = "${ModeratorRoutes.EVENT_DETAIL}/{eventId}",
            arguments = listOf(
                navArgument("eventId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""

            ModeratorEventDetailScreen(
                eventId = eventId,
                onBackClick = {
                    moderatorNavController.popBackStack()
                },
                onLogout = onLogout,
            )
        }
    }
}
