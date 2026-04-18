package com.example.proyectofinaldisenomovil.features.userFlow.Notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.AppNotification
import com.example.proyectofinaldisenomovil.domain.model.NotificationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class NotificationsUiState(
    val notifications: List<AppNotification> = emptyList(),
    val isLoading: Boolean = false,
    val unreadCount: Int = 0
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    private val _selectedFilter = MutableStateFlow("Todas")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val userId = MockDataRepository.getLoggedInUser()?.uid ?: return@launch
            val notifications = MockDataRepository.getNotificationsForUser(userId)
            val unreadCount = MockDataRepository.getUnreadNotificationCount(userId)
            
            _uiState.value = NotificationsUiState(
                notifications = notifications,
                isLoading = false,
                unreadCount = unreadCount
            )
        }
    }

    fun onFilterSelected(filter: String) {
        _selectedFilter.value = filter
    }

    fun getFilteredNotifications(): List<AppNotification> {
        val notifications = _uiState.value.notifications
        return when (_selectedFilter.value) {
            "Todas" -> notifications
            "No leídas" -> notifications.filter { !it.isRead }
            "Eventos" -> notifications.filter { 
                it.type == NotificationType.VERIFIED ||
                it.type == NotificationType.REJECTED ||
                it.type == NotificationType.NEW_EVENT ||
                it.type == NotificationType.NEW_EVENT_NEARBY
            }
            "Comentarios" -> notifications.filter { it.type == NotificationType.COMMENT }
            else -> notifications
        }
    }

    fun markAsRead(notificationId: String) {
        MockDataRepository.markNotificationAsRead(notificationId)
        loadNotifications()
    }

    fun markAllAsRead() {
        val userId = MockDataRepository.getLoggedInUser()?.uid ?: return
        MockDataRepository.markAllNotificationsAsRead(userId)
        loadNotifications()
    }

    fun getTimeAgo(timestamp: com.google.firebase.Timestamp?): String {
        if (timestamp == null) return ""
        
        val now = Calendar.getInstance().timeInMillis
        val then = timestamp.toDate().time
        val diff = now - then

        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        return when {
            minutes < 1 -> "Ahora mismo"
            minutes < 60 -> "Hace $minutes min"
            hours < 24 -> "Hace $hours h"
            days == 1L -> "Ayer"
            days < 7 -> "Hace $days días"
            else -> {
                val formatter = SimpleDateFormat("dd MMM", Locale("es", "CO"))
                formatter.format(Date(then))
            }
        }
    }

    fun getGroupedNotifications(): Map<String, List<AppNotification>> {
        val filtered = getFilteredNotifications()
        val grouped = linkedMapOf<String, MutableList<AppNotification>>()

        for (notification in filtered) {
            val section = when {
                getTimeAgo(notification.createdAt).contains("min") || 
                getTimeAgo(notification.createdAt).contains("h") && !getTimeAgo(notification.createdAt).contains("d") -> "Hoy"
                getTimeAgo(notification.createdAt) == "Ayer" -> "Ayer"
                else -> "Anteriores"
            }
            grouped.getOrPut(section) { mutableListOf() }.add(notification)
        }

        return grouped
    }
}
