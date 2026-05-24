package com.example.proyectofinaldisenomovil.features.userFlow.LikedEvents

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.data.repository.VoteRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.core.utils.ResourceProvider
import com.example.proyectofinaldisenomovil.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.Locale

data class FavoriteEvent(
    val id: String,
    val title: String,
    val category: String,
    val date: String,
    val time: String,
    val location: String,
    val distance: String,
    val attendees: Int,
    val imageUrl: String?,
    val startTimeMillis: Long? = null,
    val endTimeMillis: Long? = null
)

data class FavoritesUiState(
    val favoriteEvents: List<FavoriteEvent> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: EventCategory? = null,
    val searchQuery: String = "",
    val selectedOrder: String = ""
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val voteRepositoryImpl: VoteRepository,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
): ViewModel() {
    private var allFavoriteEvents: List<Event> = emptyList()
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(
            categories = listOf(
                resourceProvider.getString(R.string.category_sports),
                resourceProvider.getString(R.string.category_hobby),
                resourceProvider.getString(R.string.category_academic)
            ),
            selectedOrder = resourceProvider.getString(R.string.filter_name)
        )
        loadLikedEvents()
    }

    private fun loadLikedEvents() {
        viewModelScope.launch {
            val currentUser = userRepository.getLoggedInUser()
            if (currentUser != null) {
                val idLikedEvents = voteRepositoryImpl.getLikedEventsIdByUserID(currentUser.uid)
                allFavoriteEvents = eventRepository.getEventsByIds(idLikedEvents)
                applyFilters()
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }

    fun onCategorySelect(category: EventCategory?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        applyFilters()
    }

    fun onOrderSelect(order: String) {
        _uiState.value = _uiState.value.copy(selectedOrder = order)
        applyFilters()
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(
            selectedCategory = null,
            searchQuery = "",
            selectedOrder = resourceProvider.getString(R.string.filter_name)
        )
        applyFilters()
    }

    private fun applyFilters() {
        val currentState = _uiState.value
        var filtered = allFavoriteEvents

        if (currentState.selectedCategory != null) {
            filtered = filtered.filter { it.category == currentState.selectedCategory }
        }

        if (currentState.searchQuery.isNotBlank()) {
            filtered = filtered.filter { it.title.contains(currentState.searchQuery, ignoreCase = true) }
        }

        filtered = when (currentState.selectedOrder) {
            resourceProvider.getString(R.string.filter_name) -> filtered.sortedBy { it.title }
            resourceProvider.getString(R.string.filter_date) -> filtered.sortedBy { it.startDate }
            resourceProvider.getString(R.string.filter_popularity) -> filtered.sortedByDescending { it.currentAttendees }
            else -> filtered
        }

        _uiState.value = _uiState.value.copy(favoriteEvents = filtered.map { it.toFavoriteEvent() })
    }

    private fun Event.toFavoriteEvent(): FavoriteEvent {
        val currentLocale = Locale.getDefault()
        return FavoriteEvent(
            id = this.id,
            title = this.title,
            category = this.category.label,
            date = this.startDate?.let {
                val dateFormat = java.text.SimpleDateFormat(resourceProvider.getString(R.string.date_format_full), currentLocale)
                dateFormat.format(it.toDate())
            } ?: "",
            time = this.startDate?.let {
                val timeFormat = java.text.SimpleDateFormat(resourceProvider.getString(R.string.time_format), currentLocale)
                timeFormat.format(it.toDate())
            } ?: "",
            location = this.address,
            distance = "",
            attendees = this.currentAttendees,
            imageUrl = this.thumbnailUrl,
            startTimeMillis = this.startDate?.toDate()?.time,
            endTimeMillis = this.endDate?.toDate()?.time
        )
    }


    fun onToggleFavorite(eventId: String) {
        viewModelScope.launch {
            val currentUser = userRepository.getLoggedInUser()
            currentUser?.let {
                voteRepositoryImpl.toggleVote(eventId, it.uid)
                loadLikedEvents()
            }
        }
    }

    fun refresh() {
        loadLikedEvents()
    }
}
