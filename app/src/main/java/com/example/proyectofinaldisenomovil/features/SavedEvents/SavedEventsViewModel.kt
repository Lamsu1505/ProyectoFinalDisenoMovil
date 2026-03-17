package com.example.proyectofinaldisenomovil.features.SavedEvents

import androidx.lifecycle.ViewModel
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.features.LikedEvents.FavoriteEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SavedEventsUiState(
    val favoriteEvents: List<FavoriteEvent> = emptyList(),
    val categories: List<String> = listOf("Deportes", "Pasatiempo", "Academico"),
    val selectedCategory: String = "Deportes",
    val searchQuery: String = ""
)

class SavedEventsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SavedEventsUiState())
    val uiState: StateFlow<SavedEventsUiState> = _uiState.asStateFlow()

    init {
        // Datos iniciales para que la pantalla no aparezca vacía
        _uiState.value = _uiState.value.copy(
            favoriteEvents = listOf(
                FavoriteEvent(
                    "1", "Partido de la paz (R. Madrid vs Universid...", "Deportes",
                    "Jueves 19 de feb", "6:00 pm", "Estadio centenario", "2 km", 30000, R.mipmap.fut_img
                ),
                FavoriteEvent(
                    "2", "Reunion de Therians Quindio - Colombia", "Deportes",
                    "Jueves 28 de feb", "6:00 pm", "Plaza Bolivar Armenia", "4 km", 30000, R.mipmap.fut_img
                )
            )
        )
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onCategorySelect(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

}