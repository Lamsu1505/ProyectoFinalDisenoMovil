package com.example.proyectofinaldisenomovil.features.userFlow.Notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.utils.ResourceProvider
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
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    private val _selectedFilter = MutableStateFlow("")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    init {
        _selectedFilter.value = resourceProvider.getString(R.string.notifications_filter_all)
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
        }
    }

    fun onFilterSelected(filter: String) {
        _selectedFilter.value = filter
    }

    fun getFilteredNotifications(): List<AppNotification> {
        val notifications = _uiState.value.notifications
        return when (_selectedFilter.value) {
            resourceProvider.getString(R.string.notifications_filter_all) -> notifications
            resourceProvider.getString(R.string.notifications_filter_unread) -> notifications.filter { !it.isRead }
            resourceProvider.getString(R.string.notifications_filter_events) -> notifications.filter { 
                it.type == NotificationType.VERIFIED ||
                it.type == NotificationType.REJECTED ||
                it.type == NotificationType.NEW_EVENT ||
                it.type == NotificationType.NEW_EVENT_NEARBY
            }
            resourceProvider.getString(R.string.notifications_filter_comments) -> notifications.filter { it.type == NotificationType.COMMENT }
            else -> notifications
        }
    }

    fun markAsRead(notificationId: String) {
        loadNotifications()
    }

    fun markAllAsRead() {
        // loadNotifications()
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
            minutes < 1 -> resourceProvider.getString(R.string.time_now)
            minutes < 60 -> resourceProvider.getString(R.string.time_minutes_ago, minutes)
            hours < 24 -> resourceProvider.getString(R.string.time_hours_ago, hours)
            days == 1L -> resourceProvider.getString(R.string.time_yesterday)
            days < 7 -> resourceProvider.getString(R.string.time_days_ago, days)
            else -> {
                val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
                formatter.format(Date(then))
            }
        }
    }

    fun getGroupedNotifications(): Map<String, List<AppNotification>> {
        val filtered = getFilteredNotifications()
        val grouped = linkedMapOf<String, MutableList<AppNotification>>()

        for (notification in filtered) {
            val timeAgo = getTimeAgo(notification.createdAt)
            val section = when {
                timeAgo.contains(resourceProvider.getString(R.string.time_now).take(3)) || 
                (timeAgo.contains(resourceProvider.getString(R.string.time_hours_ago).split(" ")[0]) && !timeAgo.contains(resourceProvider.getString(R.string.time_days_ago).split(" ")[0])) -> resourceProvider.getString(R.string.time_today)
                timeAgo == resourceProvider.getString(R.string.time_yesterday) -> resourceProvider.getString(R.string.time_yesterday)
                else -> resourceProvider.getString(R.string.time_older)
            }
            grouped.getOrPut(section) { mutableListOf() }.add(notification)
        }

        return grouped
    }
}
