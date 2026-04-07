package com.example.proyectofinaldisenomovil.features.moderatorFlow.eventDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.core.component.moderator.state.Moderatoreventdetailuistate
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ModeratorEventDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(Moderatoreventdetailuistate())
    val uiState: StateFlow<Moderatoreventdetailuistate> = _uiState.asStateFlow()

    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // TODO: Load event from repository using eventId
            // Example:
            // eventRepository.getEventById(eventId).collect { event ->
            //     _uiState.update { it.copy(event = event, isLoading = false) }
            // }

            _uiState.update { it.copy(isLoading = false) }
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
