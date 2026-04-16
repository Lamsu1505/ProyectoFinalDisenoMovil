package com.example.proyectofinaldisenomovil.features.userFlow.ViewEvent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.utils.RequestResult
import com.example.proyectofinaldisenomovil.data.repository.AttendanceRepository
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.data.repository.VoteRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CommentUiModel(
    val id: String,
    val authorName: String,
    val initials: String,
    val timeAgo: String,
    val text: String
)


@HiltViewModel
class ViewEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val voteRepository: VoteRepository,
    private val attendanceRepository: AttendanceRepository
): ViewModel() {



    private val _detailResult = MutableStateFlow<RequestResult?>(null)
    val detailResult: StateFlow<RequestResult?> = _detailResult.asStateFlow()

    private val _currentEvent = MutableStateFlow<Event?>(null)
    val currentEvent: StateFlow<Event?> = _currentEvent.asStateFlow()


    private val _isInterested = MutableStateFlow(false)
    val isInterested: StateFlow<Boolean> = _isInterested.asStateFlow()

    private val _isConfirmed = MutableStateFlow(false)
    val isConfirmed: StateFlow<Boolean> = _isConfirmed.asStateFlow()


    fun findEventById (eventId : String){
        viewModelScope.launch {
            _currentEvent.value = null
            _detailResult.value = RequestResult.Loading
            try {
                val event = eventRepository.getEventById(eventId)
                if(event != null){
                    _currentEvent.value = event
                    val userId = MockDataRepository.getLoggedInUser()?.uid?:""
                    
                    // Inicializamos los estados de los botones desde los repositorios
                    _isInterested.value = voteRepository.hasVoted(eventId, userId)
                    _isConfirmed.value = attendanceRepository.isAttending(eventId, userId)

                    _detailResult.value = RequestResult.Success(R.string.detail_success.toString())
                }
                else{
                    _detailResult.value = RequestResult.Failure(R.string.error_unknown.toString())
                }
            } catch (e: Exception) {
                _detailResult.value = RequestResult.Failure(e.message.toString())
            }
        }

    }

    suspend fun isInterested () : Boolean{
        val eventId = _currentEvent.value?.id.toString()
        val userId = MockDataRepository.getLoggedInUser()?.uid.toString()
        return voteRepository.hasVoted(eventId , userId)
    }

    fun toggleInterested() {
        Log.i("toggleInterested", "llego al toggleViewModel")
        viewModelScope.launch {
            Log.i("toggleInterested", "llego al launch")
            Log.i("toggleInterested", "id evento " + _currentEvent.value?.id)
            Log.i("toggleInterested", "id user " + MockDataRepository.getLoggedInUser()?.uid)

            val eventId = _currentEvent.value?.id ?: return@launch
            val userId = MockDataRepository.getLoggedInUser()?.uid ?: return@launch

            // 1. Llamar al repositorio para PERSISTIR el cambio
            val success = voteRepository.toggleVote(eventId, userId)

            // 2. Sincronizar el estado de la UI con la realidad del repositorio
            if (success) {
                // Consultamos el nuevo estado directamente al repo para asegurar consistencia
                _isInterested.value = voteRepository.hasVoted(eventId, userId)
                Log.i("toggleInterested", "Finaliza en " + _isInterested.value.toString())

                // Refrescar el evento para actualizar el contador de likes en pantalla
                eventRepository.getEventById(eventId)?.let { updatedEvent ->
                    _currentEvent.value = updatedEvent
                }
            }
        }
    }

    fun toggleConfirmed() {
        viewModelScope.launch {
            val eventId = _currentEvent.value?.id ?: return@launch
            val userId = MockDataRepository.getLoggedInUser()?.uid ?: return@launch

            if (_isConfirmed.value) {
                // Si ya está confirmado, cancelamos la asistencia
                attendanceRepository.cancelAttendance(eventId, userId)
            } else {
                // Si no está confirmado, confirmamos la asistencia
                attendanceRepository.confirmAttendance(eventId, userId)
            }

            // Actualizamos el estado de confirmación consultando al repositorio
            _isConfirmed.value = attendanceRepository.isAttending(eventId, userId)

            // Refrescamos el evento para que el contador de asistentes se actualice en la tarjeta verde
            eventRepository.getEventById(eventId)?.let { updatedEvent ->
                _currentEvent.value = updatedEvent
            }
        }
    }

}
