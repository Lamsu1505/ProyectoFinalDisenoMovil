package com.vivetuzona.domain.repository

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
}
