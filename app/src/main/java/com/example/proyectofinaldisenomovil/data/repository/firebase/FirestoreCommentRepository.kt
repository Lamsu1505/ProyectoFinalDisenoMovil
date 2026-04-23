package com.example.proyectofinaldisenomovil.data.repository.firebase

import com.example.proyectofinaldisenomovil.data.repository.CommentRepository
import com.example.proyectofinaldisenomovil.domain.model.Comment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreCommentRepository @Inject constructor(
    private val db: FirebaseFirestore,
) : CommentRepository {

    override fun observeComments(eventId: String): Flow<List<Comment>> = flow {
        val snap = db.collection(COLLECTION_EVENTS).document(eventId)
            .collection(SUBCOLLECTION_COMMENTS)
            .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .get()
            .await()
        emit(snap.documents.mapNotNull { it.toCommentOrNull(eventId) }.filter { it.isVisible })
    }

    override suspend fun addComment(comment: Comment) {
        require(comment.eventId.isNotBlank()) { "Comment.eventId is required" }
        val doc = db.collection(COLLECTION_EVENTS).document(comment.eventId)
            .collection(SUBCOLLECTION_COMMENTS)
            .document()
        val created = comment.copy(
            id = doc.id,
            createdAt = comment.createdAt ?: Timestamp.now(),
        )
        doc.set(created.toFirestoreMap()).await()
    }

    override suspend fun deleteComment(eventId: String, commentId: String) {
        db.collection(COLLECTION_EVENTS).document(eventId)
            .collection(SUBCOLLECTION_COMMENTS).document(commentId)
            .delete()
            .await()
    }

    override suspend fun getCommentCount(eventId: String): Int {
        val snap = db.collection(COLLECTION_EVENTS).document(eventId)
            .collection(SUBCOLLECTION_COMMENTS)
            .get()
            .await()
        return snap.size()
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.toCommentOrNull(eventIdFallback: String): Comment? {
        val eventId = getString(FIELD_EVENT_ID) ?: eventIdFallback
        val text = getString(FIELD_TEXT) ?: return null
        return Comment(
            id = id,
            eventId = eventId,
            authorUid = getString(FIELD_AUTHOR_UID) ?: "",
            authorName = getString(FIELD_AUTHOR_NAME) ?: "",
            authorImageUrl = getString(FIELD_AUTHOR_IMAGE_URL),
            text = text,
            isInappropriate = getBoolean(FIELD_IS_INAPPROPRIATE) ?: false,
            createdAt = getTimestamp(FIELD_CREATED_AT),
        )
    }

    private fun Comment.toFirestoreMap(): Map<String, Any?> = mapOf(
        FIELD_EVENT_ID to eventId,
        FIELD_AUTHOR_UID to authorUid,
        FIELD_AUTHOR_NAME to authorName,
        FIELD_AUTHOR_IMAGE_URL to authorImageUrl,
        FIELD_TEXT to text,
        FIELD_IS_INAPPROPRIATE to isInappropriate,
        FIELD_CREATED_AT to (createdAt ?: Timestamp.now()),
    )

    private companion object {
        const val COLLECTION_EVENTS = "events"
        const val SUBCOLLECTION_COMMENTS = "comments"

        const val FIELD_EVENT_ID = "eventId"
        const val FIELD_AUTHOR_UID = "authorUid"
        const val FIELD_AUTHOR_NAME = "authorName"
        const val FIELD_AUTHOR_IMAGE_URL = "authorImageUrl"
        const val FIELD_TEXT = "text"
        const val FIELD_IS_INAPPROPRIATE = "isInappropriate"
        const val FIELD_CREATED_AT = "createdAt"
    }
}

