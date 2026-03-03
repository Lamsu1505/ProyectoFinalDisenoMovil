package com.example.proyectofinaldisenomovil.domain.usecase

import com.example.proyectofinaldisenomovil.data.model.ReputationReason
import com.example.proyectofinaldisenomovil.domain.repository.EventRepository
import com.example.proyectofinaldisenomovil.domain.repository.ReputationRepository
import com.example.proyectofinaldisenomovil.domain.repository.VoteRepository
import javax.inject.Inject

/**
 * Toggles the "Es importante" vote for the current user on [eventId].
 *
 * Side effects:
 * - Increments or decrements [Event.importantVotes] atomically.
 * - Awards [ReputationReason.VOTE_RECEIVED] to the event author on new vote.
 *
 * @return `true` if the user is now voting, `false` if the vote was removed.
 */
class CastVoteUseCase @Inject constructor(
    private val voteRepository: VoteRepository,
    private val reputationRepository: ReputationRepository,
) {
    suspend operator fun invoke(
        eventId: String,
        uid: String,
        eventAuthorUid: String,
    ): Boolean {
        val nowVoting = voteRepository.toggleVote(eventId, uid)
        if (nowVoting && uid != eventAuthorUid) {
            reputationRepository.addPoints(eventAuthorUid, ReputationReason.VOTE_RECEIVED, eventId)
        }
        return nowVoting
    }
}
