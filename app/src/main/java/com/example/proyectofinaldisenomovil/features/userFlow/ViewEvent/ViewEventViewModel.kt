package com.example.proyectofinaldisenomovil.features.userFlow.ViewEvent

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.utils.RequestResult
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
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
    private val repository: EventRepository
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
            _detailResult.value = runCatching {
                repository.getEventById(eventId)?.also {
                    Log.e("EVENT", it.toString())
                    _currentEvent.value = it
                }
            }.fold(
                onSuccess = { RequestResult.Success(R.string.detail_success.toString()) },
                onFailure = { RequestResult.Failure(it.message.toString()?: R.string.error_unknown.toString()) }
            )
        }

    }

    fun toggleInterested() {
        val eventId = _currentEvent.value?.id ?: return
        val userId = MockDataRepository.getLoggedInUser()?.uid ?: return
        _isInterested.value = MockDataRepository.toggleLikeEvent(userId, eventId)
    }

    fun toggleConfirmed() {
        val eventId = _currentEvent.value?.id ?: return
        val userId = MockDataRepository.getLoggedInUser()?.uid ?: return

        if (_isConfirmed.value) {
            if (MockDataRepository.leaveEvent(userId, eventId)) _isConfirmed.value = false
        } else {
            if (MockDataRepository.attendEvent(userId, eventId)) _isConfirmed.value = true
        }
    }

}
