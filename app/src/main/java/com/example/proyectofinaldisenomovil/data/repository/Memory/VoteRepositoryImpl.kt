package com.example.proyectofinaldisenomovil.data.repository.Memory

import android.util.Log
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.data.repository.VoteRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.User.User
import com.example.proyectofinaldisenomovil.domain.model.Vote
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoteRepositoryImpl @Inject constructor(
): VoteRepository {
    private val _votes = MutableStateFlow<List<Vote>>(emptyList())
    val votes : StateFlow<List<Vote>> = _votes.asStateFlow()

    init{
        _votes.value = fetchVotes()
    }

    override fun fetchVotes() : List<Vote>{
        return listOf(
            Vote(
                id = "vote_001",
                eventId = "event_001",
                uid = "user_001",
                createdAt = Timestamp.now()
            ),
            Vote(
                id = "vote_002",
                eventId = "event_013",
                uid = "user_001",
                createdAt = Timestamp.now()
            ),
            Vote(
                id = "vote_003",
                eventId = "event_001",
                uid = "user_003",
                createdAt = Timestamp.now()
            ),
            Vote(
                id = "vote_004",
                eventId = "event_001",
                uid = "user_002",
                createdAt = Timestamp.now()
            )
        )
    }

    override suspend fun castVote(eventId: String, uid: String) {
        val current = _votes.value.toMutableList()

        val voteId = "${eventId}_${uid}"

        val alreadyExists = current.any { it.id == voteId }
        if (!alreadyExists) {
            current.add(
                Vote(
                    id = voteId,
                    eventId = eventId,
                    uid = uid,
                    createdAt = Timestamp.now()
                )
            )
            _votes.value = current

            val event = MockDataRepository.getEventById(eventId)
            event?.let {
                MockDataRepository.updateEvent(
                    it.copy(importantVotes = it.importantVotes + 1)
                )
            }
        }
    }

    override suspend fun removeVote(eventId: String, uid: String) {
        val current = _votes.value

        val updated = current.filterNot {
            it.eventId == eventId && it.uid == uid
        }

        if (updated.size != current.size) {
            _votes.value = updated

            val event = MockDataRepository.getEventById(eventId)
            event?.let {
                MockDataRepository.updateEvent(
                    it.copy(importantVotes = (it.importantVotes - 1).coerceAtLeast(0))
                )
            }
        }
    }

    override suspend fun hasVoted(eventId: String, uid: String): Boolean {
        return _votes.value.any {
            it.eventId == eventId && it.uid == uid
        }
    }

    override suspend fun toggleVote(eventId: String, uid: String): Boolean {
        Log.i("toggleInterested", "Llego a impl")

        val currentlyVoted = hasVoted(eventId, uid)
        Log.i("toggleInterested", "Actualmente es $currentlyVoted")

        if (currentlyVoted) {
            removeVote(eventId, uid)
        } else {
            castVote(eventId, uid)
        }

        return !currentlyVoted
    }

     override suspend fun getLikedEventsIdByUserID(userId: String): List<String> {
        // 1. Filtramos los votos del StateFlow para obtener solo los del usuario solicitado
        val userVotes = _votes.value.filter { it.uid == userId }
        // 2. Extraemos los IDs de los eventos de los votos del usuario
        val eventIds = userVotes.map { it.eventId }
        // 3. Devolvemos la lista de IDs de eventos
        return eventIds
    }
}
