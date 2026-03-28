package com.example.proyectofinaldisenomovil.features.userFlow.home

import androidx.lifecycle.ViewModel
import com.example.proyectofinaldisenomovil.data.BdProvisional.MockEventData
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    // Lista completa de eventos verificados (lo que ve el feed)
    private val _events = MutableStateFlow<List<Event>>(MockEventData.getVerified())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    // Categoría seleccionada — null significa "todas"
    private val _selectedCategory = MutableStateFlow<EventCategory?>(null)
    val selectedCategory: StateFlow<EventCategory?> = _selectedCategory.asStateFlow()

    // Orden seleccionado
    private val _orderBy = MutableStateFlow("Nombre")
    val orderBy: StateFlow<String> = _orderBy.asStateFlow()

    fun onCategorySelected(category: EventCategory?) {
        _selectedCategory.value = category
        applyFilters()
    }

    fun onOrderByChanged(order: String) {
        _orderBy.value = order
        applyFilters()
    }

    fun onSearchQueryChanged(query: String) {
        applyFilters(query = query)
    }

    private fun applyFilters(query: String = "") {
        var result = MockEventData.getVerified()

        // Filtro por categoría
        _selectedCategory.value?.let { category ->
            result = result.filter { it.category == category }
        }

        // Filtro por búsqueda
        if (query.isNotBlank()) {
            result = result.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true) ||
                        it.address.contains(query, ignoreCase = true)
            }
        }

        // Orden
        result = when (_orderBy.value) {
            "Nombre"      -> result.sortedBy { it.title }
            "Popularidad" -> result.sortedByDescending { it.importantVotes }
            else          -> result // "Fecha" — cuando tengas timestamps reales
        }

        _events.value = result
    }
}