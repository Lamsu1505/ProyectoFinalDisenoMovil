package com.example.proyectofinaldisenomovil.features.moderatorFlow

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.proyectofinaldisenomovil.core.navigation.ModeratorRoutes
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.features.moderatorFlow.eventDetail.ModeratorEventDetailScreen
import com.example.proyectofinaldisenomovil.features.moderatorFlow.panel.ModeratorPanelScreen

@Composable
fun ModeratorNavigation(
    onLogout: () -> Unit,
    paddingValues: PaddingValues = PaddingValues(),
) {
    val moderatorNavController = rememberNavController()
    val navBackStackEntry by moderatorNavController.currentBackStackEntryAsState()

    NavHost(
        navController = moderatorNavController,
        startDestination = ModeratorRoutes.Panel,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable<ModeratorRoutes.Panel> {
            ModeratorPanelScreen(
                onEventClick = { eventId ->
                    moderatorNavController.navigate(ModeratorRoutes.EventDetail(eventId))
                },
                onLogout = onLogout,
            )
        }

        composable<ModeratorRoutes.EventDetail> { backStackEntry ->
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
