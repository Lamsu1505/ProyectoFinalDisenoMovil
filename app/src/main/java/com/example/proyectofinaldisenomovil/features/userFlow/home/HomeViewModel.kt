package com.example.proyectofinaldisenomovil.features.userFlow.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: EventRepository,
    private val userRepository: UserRepository
): ViewModel() {

    private var allEvents: List<Event> = emptyList()
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedCategory = MutableStateFlow<EventCategory?>(null)
    val selectedCategory: StateFlow<EventCategory?> = _selectedCategory.asStateFlow()

    private val _orderBy = MutableStateFlow("Nombre")
    val orderBy: StateFlow<String> = _orderBy.asStateFlow()

    private var currentSearchQuery = ""

    init {
        loadEvents()
        Log.d("HOME", "Usuario en sesion "+ MockDataRepository.getLoggedInUser()?.firstName)
    }

    fun loadEvents() {
        viewModelScope.launch {
            _isLoading.value = true

            //Realmente la que carga los eventos
            val verifiedEvents = repository.getVerifiedEvents()
            Log.d("HOME", "Eventos recibidos: ${verifiedEvents.size}")
            allEvents = verifiedEvents

            applyFilters()

            _isLoading.value = false
        }
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
        currentSearchQuery = query
        applyFilters()
    }

    private  fun applyFilters() {
        var result = allEvents

        _selectedCategory.value?.let { category ->
            result = result.filter { it.category == category }
        }

        if (currentSearchQuery.isNotBlank()) {
            result = result.filter {
                it.title.contains(currentSearchQuery, ignoreCase = true) ||
                        it.description.contains(currentSearchQuery, ignoreCase = true) ||
                        it.address.contains(currentSearchQuery, ignoreCase = true)
            }
        }

        result = when (_orderBy.value) {
            "Nombre" -> result.sortedBy { it.title }
            "Popularidad" -> result.sortedByDescending { it.importantVotes }
            "Fecha" -> result.sortedBy { it.startDate }
            else -> result
        }

        _events.value = result
    }

    fun refreshEvents() {
        loadEvents()
    }
}
