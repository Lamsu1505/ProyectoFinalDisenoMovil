package com.example.proyectofinaldisenomovil.data.repository.Remote

import com.example.proyectofinaldisenomovil.data.repository.VoteRepository
import com.example.proyectofinaldisenomovil.domain.model.Vote
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

@Singleton
class VoteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository
): VoteRepository {

    override suspend fun castVote(
        eventId: String,
        uid: String
    ) {
        try {
            val voteId = "${eventId}_${uid}"

            val voteRef = firestore
                .collection("votes")
                .document(voteId)

            val document = voteRef
                .get()
                .await()

            if (!document.exists()) {

                val vote = Vote(
                    id = voteId,
                    eventId = eventId,
                    uid = uid,
                    createdAt = Timestamp.now()
                )

                voteRef
                    .set(vote)
                    .await()

                firestore
                    .collection("events")
                    .document(eventId)
                    .update(
                        "importantVotes",
                        FieldValue.increment(1)
                    )
                    .await()
                
                // Award points to the voter
                userRepository.addReputationPoints(uid, 5)
                
                // Increment total likes RECEIVED for the author
                val eventDoc = firestore.collection("events").document(eventId).get().await()
                val event = eventDoc.toObject(Event::class.java)
                event?.authorUid?.let { authorId ->
                    userRepository.incrementTotalLikes(authorId, 1)
                }
            }

        } catch (e: Exception) {
            Log.e("CAST_VOTE", e.stackTraceToString())
        }
    }

    override suspend fun removeVote(
        eventId: String,
        uid: String
    ) {
        try {
            val voteId = "${eventId}_${uid}"

            val voteRef = firestore
                .collection("votes")
                .document(voteId)

            val document = voteRef
                .get()
                .await()

            if (document.exists()) {

                voteRef
                    .delete()
                    .await()

                firestore
                    .collection("events")
                    .document(eventId)
                    .update(
                        "importantVotes",
                        FieldValue.increment(-1)
                    )
                    .await()

                // Deduct points from the voter
                userRepository.addReputationPoints(uid, -5)
                
                // Decrement total likes RECEIVED for the author
                val eventDoc = firestore.collection("events").document(eventId).get().await()
                val event = eventDoc.toObject(Event::class.java)
                event?.authorUid?.let { authorId ->
                    userRepository.incrementTotalLikes(authorId, -1)
                }
            }

        } catch (e: Exception) {
            Log.e("REMOVE_VOTE", e.stackTraceToString())
        }
    }

    override suspend fun hasVoted(
        eventId: String,
        uid: String
    ): Boolean {
        return try {
            val voteId = "${eventId}_${uid}"
            firestore
                .collection("votes")
                .document(voteId)
                .get()
                .await()
                .exists()

        } catch (e: Exception) {
            false
        }
    }

    override suspend fun toggleVote(
        eventId: String,
        uid: String
    ): Boolean {

        return try {

            val currentlyVoted =
                hasVoted(eventId, uid)

            if (currentlyVoted) {
                removeVote(eventId, uid)
            } else {
                castVote(eventId, uid)
            }

            !currentlyVoted

        } catch (e: Exception) {
            Log.e("TOGGLE_VOTE", e.stackTraceToString())
            false
        }
    }

    override fun fetchVotes(): List<Vote> {
        TODO("Not yet implemented")
    }

    override suspend fun getLikedEventsIdByUserID(
        userId: String
    ): List<String> {

        return try {

            firestore
                .collection("votes")
                .whereEqualTo("uid", userId)
                .get()
                .await()
                .documents
                .mapNotNull {
                    it.getString("eventId")
                }

        } catch (e: Exception) {
            Log.e("GET_LIKED_EVENTS", e.stackTraceToString())
            emptyList()
        }
    }
}
