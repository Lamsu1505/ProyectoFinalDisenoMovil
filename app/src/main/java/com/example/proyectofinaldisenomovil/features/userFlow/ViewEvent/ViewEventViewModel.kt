package com.example.proyectofinaldisenomovil.features.userFlow.ViewEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.utils.RequestResult
import com.example.proyectofinaldisenomovil.core.utils.ResourceProvider
import com.example.proyectofinaldisenomovil.data.repository.AttendanceRepository
import com.example.proyectofinaldisenomovil.data.repository.CommentRepository
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.data.repository.VoteRepository
import com.example.proyectofinaldisenomovil.domain.model.Comment
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
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
    private val resourceProvider: ResourceProvider,
    private val userRepository: UserRepository
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

    fun findEventById(eventId: String) {
        viewModelScope.launch {
            _currentEvent.value = null
            _detailResult.value = RequestResult.Loading
            try {
                val event = eventRepository.getEventById(eventId)
                if (event != null) {
                    _currentEvent.value = event
                    val userId = userRepository.getLoggedInUser()?.uid ?: ""
                    
                    _isInterested.value = voteRepository.hasVoted(eventId, userId)
                    _isConfirmed.value = attendanceRepository.isAttending(eventId, userId)

                    _detailResult.value = RequestResult.Success(resourceProvider.getString(R.string.detail_success))
                    
                    observeComments(eventId)
                } else {
                    _detailResult.value = RequestResult.Failure(resourceProvider.getString(R.string.error_unknown))
                }
            } catch (e: Exception) {
                _detailResult.value = RequestResult.Failure(e.message.toString())
            }
        }
    }

    private fun observeComments(eventId: String) {
        viewModelScope.launch {
            commentRepository.observeComments(eventId).collectLatest { domainComments ->
                _comments.value = domainComments.filter { it.isVisible }.map { it.toUiModel() }
            }
        }
    }

    fun addComment(text: String) {
        val eventId = _currentEvent.value?.id ?: return
        viewModelScope.launch {
            val user = userRepository.getLoggedInUser() ?: return@launch
            val comment = Comment(
                eventId = eventId,
                authorUid = user.uid,
                authorName = user.fullName,
                authorImageUrl = user.profileImageUrl,
                text = text.trim(),
                createdAt = Timestamp.now()
            )
            commentRepository.addComment(comment)
        }
    }

    private fun Comment.toUiModel(): CommentUiModel {
        val nameParts = authorName.split(" ").filter { it.isNotBlank() }
        val initials = when {
            nameParts.size >= 2 -> "${nameParts[0].take(1)}${nameParts[1].take(1)}"
            nameParts.size == 1 -> nameParts[0].take(2)
            else -> "U"
        }.uppercase()

        return CommentUiModel(
            id = id,
            authorName = authorName,
            initials = initials,
            timeAgo = getTimeAgo(createdAt),
            text = text
        )
    }

    private fun getTimeAgo(timestamp: Timestamp?): String {
        if (timestamp == null) return ""
        val now = Date().time
        val then = timestamp.toDate().time
        val diff = now - then

        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        return when {
            minutes < 1 -> resourceProvider.getString(R.string.time_now)
            minutes < 60 -> resourceProvider.getString(R.string.time_minutes_ago, minutes)
            hours < 24 -> resourceProvider.getString(R.string.time_hours_ago, hours)
            else -> resourceProvider.getString(R.string.time_days_ago, days)
        }
    }

    fun toggleInterested() {
        viewModelScope.launch {
            val eventId = _currentEvent.value?.id ?: return@launch
            val userId = userRepository.getLoggedInUser()?.uid ?: return@launch
            val isNowInterested = voteRepository.toggleVote(eventId, userId)
            _isInterested.value = isNowInterested
            eventRepository.getEventById(eventId)?.let { updatedEvent ->
                _currentEvent.value = updatedEvent
            }
        }
    }

    fun toggleConfirmed() {
        viewModelScope.launch {
            val eventId = _currentEvent.value?.id ?: return@launch
            val userId = userRepository.getLoggedInUser()?.uid ?: return@launch

            if (_isConfirmed.value) {
                attendanceRepository.cancelAttendance(eventId, userId)
            } else {
                attendanceRepository.confirmAttendance(eventId, userId)
            }

            _isConfirmed.value = attendanceRepository.isAttending(eventId, userId)
            eventRepository.getEventById(eventId)?.let { updatedEvent ->
                _currentEvent.value = updatedEvent
            }
        }
    }
}
