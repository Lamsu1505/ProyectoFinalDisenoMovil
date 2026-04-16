package com.example.proyectofinaldisenomovil.features.userFlow.LikedEvents

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.Memory.VoteRepositoryImpl
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoriteEvent(
    val id: String,
    val title: String,
    val category: String,
    val date: String,
    val time: String,
    val location: String,
    val distance: String,
    val attendees: Int,
    val imageUrl: String?
)

data class FavoritesUiState(
    val favoriteEvents: List<FavoriteEvent> = emptyList(),
    val categories: List<String> = listOf("Deportes", "Pasatiempo", "Academico"),
    val selectedCategory: String = "Deportes",
    val searchQuery: String = ""
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val voteRepositoryImpl: VoteRepositoryImpl
): ViewModel() {
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        Log.i("liked events screen" , "llego al init")
        loadLikedEvents()
    }

    private fun loadLikedEvents() {
        viewModelScope.launch {
            Log.i("liked events screen" , "entro al launch")
            val currentUser = MockDataRepository.getLoggedInUser()
            Log.i("liked events screen" , "capturo al usuario " + currentUser.toString())

            if (currentUser != null) {
                val idLikedEvents = voteRepositoryImpl.getLikedEventsIdByUserID(currentUser.uid)
                Log.i("Liked events" , "el usuario " + currentUser.uid + " tiene " + idLikedEvents.size.toString() + " eventos favoritos")

                val likedEvents = eventRepository.getEventsByIds(idLikedEvents)
                _uiState.value = _uiState.value.copy(
                    favoriteEvents = likedEvents.map { it.toFavoriteEvent() }
                )
            }
        }
    }

    private fun Event.toFavoriteEvent(): FavoriteEvent {
        return FavoriteEvent(
            id = this.id,
            title = this.title,
            category = this.category.label,
            date = this.startDate?.let {
                val dateFormat = java.text.SimpleDateFormat("EEEE d 'de' MMMM", java.util.Locale("es", "CO"))
                dateFormat.format(it.toDate())
            } ?: "",
            time = this.startDate?.let {
                val timeFormat = java.text.SimpleDateFormat("h:mm a", java.util.Locale("es", "CO"))
                timeFormat.format(it.toDate())
            } ?: "",
            location = this.address,
            distance = "",
            attendees = this.currentAttendees,
            imageUrl = this.thumbnailUrl
        )
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onCategorySelect(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun onToggleFavorite(eventId: String) {
        viewModelScope.launch {
            val currentUser = MockDataRepository.getLoggedInUser()
            currentUser?.let {
                MockDataRepository.toggleLikeEvent(it.uid, eventId)
                loadLikedEvents()
            }
        }
    }

    fun refresh() {
        loadLikedEvents()
    }
}
