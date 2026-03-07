package com.example.proyectofinaldisenomovil.features.Notifications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class NotificationType {
    COMMENT,
    EVENT_REJECTED,
    EVENT_VERIFIED,
    NEW_EVENT_NEARBY
}

data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val title: String,
    val subtitle: String,
    val timeAgo: String,
    val isUnread: Boolean = false,
    val initials: String = ""
)

data class NotificationsUiState(
    val notifications: List<NotificationItem> = listOf(
        // Hoy
        NotificationItem(
            id = "1",
            type = NotificationType.COMMENT,
            title = "Nuevo comentario",
            subtitle = "Santiago Londoño comentó en \"Partido de la histo...\"",
            timeAgo = "Hace 50 min",
            isUnread = true,
            initials = "SL"
        ),
        NotificationItem(
            id = "2",
            type = NotificationType.EVENT_REJECTED,
            title = "Evento rechazado",
            subtitle = "Tu evento fue rechazado, revisa los motivos.",
            timeAgo = "Hace 2 h",
            isUnread = true
        ),
        NotificationItem(
            id = "3",
            type = NotificationType.EVENT_VERIFIED,
            title = "Evento Verificado",
            subtitle = "Tu evento \"Partido de la historia...\" fue aceptado",
            timeAgo = "Hace 2 h",
            isUnread = false
        ),
        // Ayer
        NotificationItem(
            id = "4",
            type = NotificationType.NEW_EVENT_NEARBY,
            title = "Nuevo evento cerca de ti",
            subtitle = "\"Encuentro de therians\" cerca de ti",
            timeAgo = "Hace 22 h",
            isUnread = true
        ),
        NotificationItem(
            id = "5",
            type = NotificationType.NEW_EVENT_NEARBY,
            title = "Nuevo evento cerca de ti",
            subtitle = "\"Encuentro de therians\" cerca de ti",
            timeAgo = "Hace 22 h",
            isUnread = false
        ),
        // Hace unos dias
        NotificationItem(
            id = "6",
            type = NotificationType.NEW_EVENT_NEARBY,
            title = "Nuevo evento cerca de ti",
            subtitle = "\"Encuentro de therians\" cerca de ti",
            timeAgo = "Hace 22 h",
            isUnread = false
        ),
        NotificationItem(
            id = "7",
            type = NotificationType.NEW_EVENT_NEARBY,
            title = "Nuevo evento cerca de ti",
            subtitle = "\"Encuentro de therians\" cerca de ti",
            timeAgo = "Hace 22 h",
            isUnread = false
        )
    )
)

class NotificationsViewModel : ViewModel() {

    var uiState by mutableStateOf(NotificationsUiState())
        private set

    var selectedCategory by mutableStateOf("Todas")
        private set

    fun selectCategory(category: String) {
        selectedCategory = category
    }

    fun markAllAsRead() {
        uiState = uiState.copy(
            notifications = uiState.notifications.map { it.copy(isUnread = false) }
        )
    }

    fun getFilteredNotifications(): List<NotificationItem> {
        return when (selectedCategory) {
            "Todas" -> uiState.notifications
            "No leidas" -> uiState.notifications.filter { it.isUnread }
            "Eventos" -> uiState.notifications.filter {
                it.type == NotificationType.EVENT_REJECTED ||
                it.type == NotificationType.EVENT_VERIFIED ||
                it.type == NotificationType.NEW_EVENT_NEARBY
            }
            "Coments" -> uiState.notifications.filter {
                it.type == NotificationType.COMMENT
            }
            else -> uiState.notifications
        }
    }

    /**
     * Groups notifications into sections: "Hoy", "Ayer", "Hace unos dias"
     */
    fun getGroupedNotifications(): Map<String, List<NotificationItem>> {
        val filtered = getFilteredNotifications()
        val grouped = linkedMapOf<String, MutableList<NotificationItem>>()

        for (notification in filtered) {
            val section = when {
                notification.timeAgo.contains("min") || (notification.timeAgo.contains("h") && !notification.timeAgo.contains("22")) -> "Hoy"
                notification.timeAgo.contains("22 h") && (notification.id == "4" || notification.id == "5") -> "Ayer"
                else -> "Hace unos dias"
            }
            grouped.getOrPut(section) { mutableListOf() }.add(notification)
        }

        return grouped
    }
}
