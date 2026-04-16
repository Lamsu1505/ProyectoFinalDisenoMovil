package com.example.proyectofinaldisenomovil.data.repository.Memory

import android.util.Log
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.data.repository.VoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoteRepositoryImpl @Inject constructor(): VoteRepository {
    
    // Almacenamos los votos en memoria: eventId -> Set de UserIds que votaron
    private val votesMap = mutableMapOf<String, MutableSet<String>>()

    override suspend fun castVote(eventId: String, uid: String) {
        val eventVotes = votesMap.getOrPut(eventId) { mutableSetOf() }
        if (eventVotes.add(uid)) {
            // Si el voto es nuevo, incrementamos el contador en el MockDataRepository
            val event = MockDataRepository.getEventById(eventId)
            event?.let {
                MockDataRepository.updateEvent(it.copy(importantVotes = it.importantVotes + 1))
            }
        }
    }

    override suspend fun removeVote(eventId: String, uid: String) {
        val eventVotes = votesMap[eventId]
        if (eventVotes?.remove(uid) == true) {
            // Si el voto existía y se borró, decrementamos el contador
            val event = MockDataRepository.getEventById(eventId)
            event?.let {
                MockDataRepository.updateEvent(it.copy(importantVotes = (it.importantVotes - 1).coerceAtLeast(0)))
            }
        }
    }

    override suspend fun hasVoted(eventId: String, uid: String): Boolean {
        return votesMap[eventId]?.contains(uid) ?: false
    }

    override suspend fun toggleVote(eventId: String, uid: String): Boolean {
        Log.i("toggleInterested", "Llego a impl")
        val currentlyVoted = hasVoted(eventId, uid)
        Log.i("toggleInterested", "Actualmente es " + currentlyVoted)
        if (currentlyVoted) {
            removeVote(eventId, uid)
        } else {
            castVote(eventId, uid)
        }
        return true
    }
}
