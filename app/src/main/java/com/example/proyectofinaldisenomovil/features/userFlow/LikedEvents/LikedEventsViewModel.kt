package com.example.proyectofinaldisenomovil.features.userFlow.LikedEvents

import androidx.lifecycle.ViewModel
import com.example.proyectofinaldisenomovil.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FavoriteEvent(
    val id: String,
    val title: String,
    val category: String,
    val date: String,
    val time: String,
    val location: String,
    val distance: String,
    val attendees: Int,
    val imageRes: Int
)

data class FavoritesUiState(
    val favoriteEvents: List<FavoriteEvent> = emptyList(),
    val categories: List<String> = listOf("Deportes", "Pasatiempo", "Academico"),
    val selectedCategory: String = "Deportes",
    val searchQuery: String = ""
)

class FavoritesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        // Data quemada inicial
        _uiState.value = _uiState.value.copy(
            favoriteEvents = listOf(
                FavoriteEvent(
                    "1", "Partido de la paz (R. Madrid vs Universid...", "Deportes",
                    "Jueves 19 de feb", "6:00 pm", "Estadio centenario", "2 km", 30000, R.mipmap.fut_img
                ),
                FavoriteEvent(
                    "2", "Reunion de Therians Quindio - Colombia", "Deportes",
                    "Jueves 28 de feb", "6:00 pm", "Plaza Bolivar Armenia", "4 km", 30000, R.mipmap.fut_img
                ),
                FavoriteEvent(
                    "3", "Torneo de Ajedrez Universitario", "Academico",
                    "Viernes 5 de mar", "2:00 pm", "Biblioteca Central", "1 km", 50, R.mipmap.fut_img
                ),
                FavoriteEvent(
                    "4", "Clase de Yoga al Aire Libre", "Pasatiempo",
                    "Sabado 6 de mar", "8:00 am", "Parque de la Vida", "3 km", 100, R.mipmap.fut_img
                ),
                FavoriteEvent(
                    "5", "Concierto Rock Local", "Pasatiempo",
                    "Domingo 7 de mar", "7:00 pm", "Teatro Cruz Roja", "5 km", 500, R.mipmap.fut_img
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

    fun onToggleFavorite(eventId: String) {
        // Lógica para quitar de favoritos
        val updatedList = _uiState.value.favoriteEvents.filter { it.id != eventId }
        _uiState.value = _uiState.value.copy(favoriteEvents = updatedList)
    }
}
