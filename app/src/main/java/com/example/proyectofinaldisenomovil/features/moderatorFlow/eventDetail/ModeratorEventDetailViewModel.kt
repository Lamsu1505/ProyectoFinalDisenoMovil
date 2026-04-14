package com.example.proyectofinaldisenomovil.features.moderatorFlow.eventDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.core.component.moderator.state.Moderatoreventdetailuistate
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModeratorEventDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val eventId: String = savedStateHandle.get<String>("eventId") ?: ""

    private val _uiState = MutableStateFlow(Moderatoreventdetailuistate())
    val uiState: StateFlow<Moderatoreventdetailuistate> = _uiState.asStateFlow()

    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val event = MockDataRepository.getEventById(eventId)

            _uiState.update {
                it.copy(
                    event = event,
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
        val event = _uiState.value.event ?: return

        if (currentReason.isBlank()) {
            _uiState.update { it.copy(rejectionReasonError = "Por favor ingrese un motivo") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingVerification = true) }

            MockDataRepository.rejectEvent(event.id, currentReason)

            val updatedEvent = MockDataRepository.getEventById(event.id)
            _uiState.update {
                it.copy(
                    event = updatedEvent,
                    isSubmittingVerification = false,
                    showRejectDialog = false,
                )
            }
        }
    }

    fun onAcceptEvent() {
        val event = _uiState.value.event ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingVerification = true) }

            MockDataRepository.approveEvent(event.id)

            val updatedEvent = MockDataRepository.getEventById(event.id)
            _uiState.update {
                it.copy(
                    event = updatedEvent,
                    isSubmittingVerification = false,
                )
            }
        }
    }

    fun onLogoutClick() {
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    fun onLogoutDismiss() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }
}