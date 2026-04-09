package com.example.proyectofinaldisenomovil.features.moderatorFlow.eventDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.core.component.moderator.state.Moderatoreventdetailuistate
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ModeratorEventDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val eventId: String = savedStateHandle.get<String>("eventId") ?: ""

    private val _uiState = MutableStateFlow(Moderatoreventdetailuistate())
    val uiState: StateFlow<Moderatoreventdetailuistate> = _uiState.asStateFlow()

    init {
        loadEvent(eventId)
    }

    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            delay(500)

            // Sample event data for testing
            val sampleEvent = Event(
                id = eventId,
                title = "Concierto de Rock en el Parque",
                description = "Disfruta de una noche llena de música y energía con las mejores bandas de rock locales e internacionales. El evento incluirá食物饮品 y actividades para toda la familia. No te pierdas esta oportunidad única de escuchar a tus bandas favoritas en vivo.\n\nHabrá food trucks, zonas de descanso y un mercado de artesanías.",
                category = EventCategory.CULTURA,
                startDate = Timestamp(Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)),
                endDate = Timestamp(Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000 + 3 * 60 * 60 * 1000)),
                address = "Parque Central, Av. Principal 123, Ciudad de Example",
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1470229722913-7c0e2dbbafd3?w=800",
                    "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=800",
                    "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800"
                ),
                currentAttendees = 45,
                maxAttendees = 200,
            )

            _uiState.update {
                it.copy(
                    event = sampleEvent,
                    isLoading = false,
                )
            }
        }
    }

    fun onImageIndexChange(index: Int) {
        _uiState.update { it.copy(currentImageIndex = index) }
    }

    fun onRejectClick() {
        _uiState.update { it.copy(showRejectDialog = true) }
    }

    fun onRejectDialogDismiss() {
        _uiState.update { it.copy(showRejectDialog = false, rejectionReason = "", rejectionReasonError = null) }
    }

    fun onRejectionReasonChange(reason: String) {
        _uiState.update { it.copy(rejectionReason = reason, rejectionReasonError = null) }
    }

    fun onRejectConfirm() {
        val currentReason = _uiState.value.rejectionReason

        if (currentReason.isBlank()) {
            _uiState.update { it.copy(rejectionReasonError = "Por favor ingrese un motivo") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingVerification = true) }

            delay(1000)

            // TODO: Call repository to reject event
            // eventRepository.rejectEvent(eventId, currentReason)

            _uiState.update {
                it.copy(
                    isSubmittingVerification = false,
                    showRejectDialog = false,
                )
            }
        }
    }

    fun onAcceptEvent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingVerification = true) }

            delay(1000)

            // TODO: Call repository to accept event
            // eventRepository.verifyEvent(eventId)

            _uiState.update { it.copy(isSubmittingVerification = false) }
        }
    }

    fun onLogoutClick() {
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    fun onLogoutDismiss() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }
}