package com.example.proyectofinaldisenomovil.features.userFlow.Notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.utils.ResourceProvider
import com.example.proyectofinaldisenomovil.data.repository.NotificationRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.AppNotification
import com.example.proyectofinaldisenomovil.domain.model.NotificationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

enum class NotificationFilter {
    ALL, UNREAD, EVENTS, COMMENTS
}

data class NotificationsUiState(
    val notifications: List<AppNotification> = emptyList(),
    val isLoading: Boolean = false,
    val unreadCount: Int = 0
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    private val _selectedFilter = MutableStateFlow(NotificationFilter.ALL)
    val selectedFilter: StateFlow<NotificationFilter> = _selectedFilter.asStateFlow()

    init {
        observeNotifications()
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            val user = userRepository.getLoggedInUser()
            user?.let { currentUser ->
                _uiState.value = _uiState.value.copy(isLoading = true)
                notificationRepository.observeNotifications(currentUser.uid).collectLatest { notifications ->
                    _uiState.value = _uiState.value.copy(
                        notifications = notifications,
                        unreadCount = notifications.count { !it.read },
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onFilterSelected(filter: NotificationFilter) {
        _selectedFilter.value = filter
    }

    val groupedNotifications: StateFlow<Map<String, List<AppNotification>>> =
        combine(_uiState, _selectedFilter) { state, filter ->
            val filtered = when (filter) {
                NotificationFilter.ALL -> state.notifications
                NotificationFilter.UNREAD -> state.notifications.filter { !it.read }
                NotificationFilter.EVENTS -> state.notifications.filter {
                    it.type == NotificationType.VERIFIED ||
                    it.type == NotificationType.REJECTED ||
                    it.type == NotificationType.NEW_EVENT ||
                    it.type == NotificationType.NEW_EVENT_NEARBY ||
                    it.type == NotificationType.EDITED ||
                    it.type == NotificationType.SAVE ||
                    it.type == NotificationType.LIKE ||
                    it.type == NotificationType.FINALIZED
                }
                NotificationFilter.COMMENTS -> state.notifications.filter { it.type == NotificationType.COMMENT }
            }

            val grouped = linkedMapOf<String, MutableList<AppNotification>>()
            for (notification in filtered) {
                val section = getSectionTitle(notification.createdAt)
                grouped.getOrPut(section) { mutableListOf() }.add(notification)
            }
            grouped
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    private fun getSectionTitle(timestamp: com.google.firebase.Timestamp?): String {
        if (timestamp == null) return resourceProvider.getString(R.string.time_older)

        val now = Calendar.getInstance()
        val created = Calendar.getInstance().apply { time = timestamp.toDate() }

        return when {
            isSameDay(now, created) -> resourceProvider.getString(R.string.time_today)
            isYesterday(now, created) -> resourceProvider.getString(R.string.time_yesterday)
            else -> resourceProvider.getString(R.string.time_older)
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            notificationRepository.markAsRead(notificationId)
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            val user = userRepository.getLoggedInUser()
            user?.let {
                notificationRepository.markAllAsRead(it.uid)
            }
        }
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

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(now: Calendar, created: Calendar): Boolean {
        val yesterday = Calendar.getInstance().apply {
            time = now.time
            add(Calendar.DAY_OF_YEAR, -1)
        }
        return isSameDay(yesterday, created)
    }
}
