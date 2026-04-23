package com.example.proyectofinaldisenomovil.data.repository.firebase

import com.example.proyectofinaldisenomovil.data.repository.VoteRepository
import com.example.proyectofinaldisenomovil.domain.model.Vote
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreVoteRepository @Inject constructor(
    private val db: FirebaseFirestore,
) : VoteRepository {

    override suspend fun castVote(eventId: String, uid: String) {
        val voteRef = db.collection(COLLECTION_EVENTS).document(eventId)
            .collection(SUBCOLLECTION_VOTES).document(uid)
        val eventRef = db.collection(COLLECTION_EVENTS).document(eventId)

        db.runTransaction { tx ->
            val existing = tx.get(voteRef)
            if (!existing.exists()) {
                tx.set(
                    voteRef,
                    mapOf(
                        FIELD_UID to uid,
                        FIELD_EVENT_ID to eventId,
                        FIELD_CREATED_AT to Timestamp.now(),
                    )
                )
                tx.update(eventRef, FIELD_IMPORTANT_VOTES, FieldValue.increment(1))
            }
            null
        }.await()
    }

    override suspend fun removeVote(eventId: String, uid: String) {
        val voteRef = db.collection(COLLECTION_EVENTS).document(eventId)
            .collection(SUBCOLLECTION_VOTES).document(uid)
        val eventRef = db.collection(COLLECTION_EVENTS).document(eventId)

        db.runTransaction { tx ->
            val existing = tx.get(voteRef)
            if (existing.exists()) {
                tx.delete(voteRef)
                tx.update(eventRef, FIELD_IMPORTANT_VOTES, FieldValue.increment(-1))
            }
            null
        }.await()
    }

    override suspend fun hasVoted(eventId: String, uid: String): Boolean {
        val snap = db.collection(COLLECTION_EVENTS).document(eventId)
            .collection(SUBCOLLECTION_VOTES).document(uid)
            .get()
            .await()
        return snap.exists()
    }

    override suspend fun toggleVote(eventId: String, uid: String): Boolean {
        val currentlyVoted = hasVoted(eventId, uid)
        if (currentlyVoted) removeVote(eventId, uid) else castVote(eventId, uid)
        return !currentlyVoted
    }

    override fun fetchVotes(): List<Vote> {
        // Legacy synchronous API — not used once Firestore is wired everywhere.
        return emptyList()
    }

    override suspend fun getLikedEventsIdByUserID(userId: String): List<String> {
        val query = db.collectionGroup(SUBCOLLECTION_VOTES)
            .whereEqualTo(FIELD_UID, userId)
            .get()
            .await()
        return query.documents.mapNotNull { it.getString(FIELD_EVENT_ID) }.distinct()
    }

    private companion object {
        const val COLLECTION_EVENTS = "events"
        const val SUBCOLLECTION_VOTES = "votes"

        const val FIELD_UID = "uid"
        const val FIELD_EVENT_ID = "eventId"
        const val FIELD_CREATED_AT = "createdAt"

        const val FIELD_IMPORTANT_VOTES = "importantVotes"
    }
}

