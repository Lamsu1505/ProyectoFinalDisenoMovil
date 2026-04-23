package com.example.proyectofinaldisenomovil.features.userFlow.ViewEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.utils.RequestResult
import com.example.proyectofinaldisenomovil.core.utils.ResourceProvider
import com.example.proyectofinaldisenomovil.data.repository.AttendanceRepository
import com.example.proyectofinaldisenomovil.data.repository.CommentRepository
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.data.repository.VoteRepository
import com.example.proyectofinaldisenomovil.domain.model.Comment
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
    private val attendanceRepository: AttendanceRepository,
    private val commentRepository: CommentRepository,
    private val resourceProvider: ResourceProvider
): ViewModel() {



    private val _detailResult = MutableStateFlow<RequestResult?>(null)
    val detailResult: StateFlow<RequestResult?> = _detailResult.asStateFlow()

    private val _currentEvent = MutableStateFlow<Event?>(null)
    val currentEvent: StateFlow<Event?> = _currentEvent.asStateFlow()


    private val _isInterested = MutableStateFlow(false)
    val isInterested: StateFlow<Boolean> = _isInterested.asStateFlow()

    private val _isConfirmed = MutableStateFlow(false)
    val isConfirmed: StateFlow<Boolean> = _isConfirmed.asStateFlow()

    private val _comments = MutableStateFlow<List<CommentUiModel>>(emptyList())
    val comments: StateFlow<List<CommentUiModel>> = _comments.asStateFlow()

    private val _newCommentText = MutableStateFlow("")
    val newCommentText: StateFlow<String> = _newCommentText.asStateFlow()

    fun onNewCommentChange(text: String) {
        _newCommentText.value = text
    }


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

                    loadComments(eventId)
                    _detailResult.value = RequestResult.Success(resourceProvider.getString(R.string.detail_success))
                }
                else{
                    _detailResult.value = RequestResult.Failure(resourceProvider.getString(R.string.error_unknown))
                }
            } catch (e: Exception) {
                _detailResult.value = RequestResult.Failure(e.message.toString())
            }
        }

    }

    private suspend fun loadComments(eventId: String) {
        commentRepository.observeComments(eventId).collect { list ->
            _comments.value = list.map { it.toUiModel() }
        }
    }

    fun sendComment() {
        viewModelScope.launch {
            val eventId = _currentEvent.value?.id ?: return@launch
            val currentUser = MockDataRepository.getLoggedInUser() ?: return@launch
            val text = _newCommentText.value.trim()
            if (text.isBlank()) return@launch

            commentRepository.addComment(
                Comment(
                    eventId = eventId,
                    authorUid = currentUser.uid,
                    authorName = currentUser.fullName,
                    authorImageUrl = currentUser.profileImageUrl,
                    text = text,
                    isInappropriate = false,
                    createdAt = null,
                )
            )
            _newCommentText.value = ""
            // reload one-shot
            loadComments(eventId)
        }
    }

    suspend fun isInterested () : Boolean{
        val eventId = _currentEvent.value?.id.toString()
        val userId = MockDataRepository.getLoggedInUser()?.uid.toString()
        return voteRepository.hasVoted(eventId , userId)
    }

    fun toggleInterested() {
        viewModelScope.launch {
            val eventId = _currentEvent.value?.id ?: return@launch
            val userId = MockDataRepository.getLoggedInUser()?.uid ?: return@launch

            // 1. Ejecutamos el cambio y obtenemos el nuevo estado (true o false)
            val isNowInterested = voteRepository.toggleVote(eventId, userId)

            // 2. Sincronizamos los estados del ViewModel sin importar si es true o false
            _isInterested.value = isNowInterested

            // 3. Refrescamos el evento para que el contador de likes se actualice en la tarjeta
            eventRepository.getEventById(eventId)?.let { updatedEvent ->
                _currentEvent.value = updatedEvent
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

    private fun Comment.toUiModel(): CommentUiModel {
        val initials = authorName
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifBlank { "U" }

        val timeAgo = createdAt?.toDate()?.let { date ->
            val diff = System.currentTimeMillis() - date.time
            val minutes = diff / 60000
            when {
                minutes < 1 -> "Ahora"
                minutes < 60 -> "Hace ${minutes}m"
                minutes < 1440 -> "Hace ${minutes / 60}h"
                else -> "Hace ${minutes / 1440}d"
            }
        } ?: ""

        return CommentUiModel(
            id = id,
            authorName = authorName,
            initials = initials,
            timeAgo = timeAgo,
            text = text,
        )
    }
}
