package com.example.proyectofinaldisenomovil.data.repository

import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.User.User
import com.example.proyectofinaldisenomovil.domain.model.Vote

/**
 * Contract for "Es importante / Me interesa" vote persistence.
 *
 * Implemented by [com.vivetuzona.data.repository.FirebaseVoteRepository].
 */
interface VoteRepository {

    /**
     * Casts a vote for [eventId] by [uid] and increments [Event.importantVotes].
     * No-op (idempotent) if a vote already exists.
     */
    suspend fun castVote(eventId: String, uid: String)

    /**
     * Removes the vote and decrements [Event.importantVotes].
     * No-op if no vote exists.
     */
    suspend fun removeVote(eventId: String, uid: String)

    /** Returns `true` when the user has already voted on this event. */
    suspend fun hasVoted(eventId: String, uid: String): Boolean

    /** Toggles the vote state based on [hasVoted]. Returns new voted state. */
    suspend fun toggleVote(eventId: String, uid: String): Boolean
    fun fetchVotes(): List<Vote>

    suspend fun getLikedEventsIdByUserID(userId: String): List<String>
}
