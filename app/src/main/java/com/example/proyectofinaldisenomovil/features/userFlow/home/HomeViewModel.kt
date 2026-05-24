package com.example.proyectofinaldisenomovil.features.userFlow.home

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


data class HomeUiState(
    val likedEvents: Map<String, Boolean> = emptyMap(),
    val sortOptions: List<String> = emptyList()
)


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: EventRepository,
    private val userRepository: UserRepository,
    private val voteRepository: VoteRepository,
    private val resourceProvider: ResourceProvider
): ViewModel() {

    private var allEvents: List<Event> = emptyList()
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedCategory = MutableStateFlow<EventCategory?>(null)
    val selectedCategory: StateFlow<EventCategory?> = _selectedCategory.asStateFlow()

    private val _orderBy = MutableStateFlow("")
    val orderBy: StateFlow<String> = _orderBy.asStateFlow()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        val nameOption = resourceProvider.getString(R.string.filter_name)
        val popularityOption = resourceProvider.getString(R.string.filter_popularity)
        val dateOption = resourceProvider.getString(R.string.filter_date)
        
        _uiState.value = _uiState.value.copy(
            sortOptions = listOf(nameOption, dateOption, popularityOption)
        )
        _orderBy.value = nameOption
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val verifiedEvents = repository.getVerifiedEvents()
                allEvents = verifiedEvents
                loadLikedState(verifiedEvents)
                applyFilters()
            } catch (e: Exception) {
                Log.e("HOME", "Error loading events", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadLikedState(events: List<Event>) {
        val userId = userRepository.getLoggedInUser()?.uid ?: return
        val likesMap = mutableMapOf<String, Boolean>()
        events.forEach { event ->
            likesMap[event.id] = voteRepository.hasVoted(event.id, userId)
        }
        _uiState.value = _uiState.value.copy(likedEvents = likesMap)
    }

    fun onCategorySelected(category: EventCategory?) {
        _selectedCategory.value = category
        applyFilters()
    }

    fun onOrderByChanged(order: String) {
        _orderBy.value = order
        applyFilters()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun clearFilters() {
        _selectedCategory.value = null
        _searchQuery.value = ""
        _orderBy.value = resourceProvider.getString(R.string.filter_name)
        applyFilters()
    }

    private fun applyFilters() {
        var result = allEvents

        _selectedCategory.value?.let { category ->
            result = result.filter { it.category == category }
        }

        if (_searchQuery.value.isNotBlank()) {
            result = result.filter {
                it.title.contains(_searchQuery.value, ignoreCase = true) ||
                        it.description.contains(_searchQuery.value, ignoreCase = true) ||
                        it.address.contains(_searchQuery.value, ignoreCase = true)
            }
        }

        result = when (_orderBy.value) {
            resourceProvider.getString(R.string.filter_name) -> result.sortedBy { it.title }
            resourceProvider.getString(R.string.filter_popularity) -> result.sortedByDescending { it.importantVotes }
            resourceProvider.getString(R.string.filter_date) -> result.sortedBy { it.startDate }
            else -> result
        }

        _events.value = result
    }

    fun refreshEvents() {
        loadEvents()
    }

    fun onLikeClick(eventId: String) {
        viewModelScope.launch {
            val userId = userRepository.getLoggedInUser()?.uid ?: return@launch
            val isNowInterested = voteRepository.toggleVote(eventId, userId)
            _uiState.value = _uiState.value.copy(
                likedEvents = _uiState.value.likedEvents.toMutableMap().apply {
                    this[eventId] = isNowInterested
                }
            )
        }
    }
}
