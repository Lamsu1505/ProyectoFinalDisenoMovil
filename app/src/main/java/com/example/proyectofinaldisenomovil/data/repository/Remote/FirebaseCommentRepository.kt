package com.example.proyectofinaldisenomovil.data.repository.Remote

import android.util.Log
import com.example.proyectofinaldisenomovil.data.repository.CommentRepository
import com.example.proyectofinaldisenomovil.data.repository.NotificationRepository
import com.example.proyectofinaldisenomovil.domain.model.AppNotification
import com.example.proyectofinaldisenomovil.domain.model.Comment
import com.example.proyectofinaldisenomovil.domain.model.NotificationType
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseCommentRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val notificationRepository: NotificationRepository
) : CommentRepository {

    override fun observeComments(eventId: String): Flow<List<Comment>> = callbackFlow {
        val subscription = firestore.collection("events")
            .document(eventId)
            .collection("comments")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirebaseCommentRepo", "Error observing comments", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val comments = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Comment::class.java)?.copy(id = doc.id)
                    }
                    trySend(comments)
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun addComment(comment: Comment) {
        try {
            val eventRef = firestore.collection("events").document(comment.eventId)
            eventRef.collection("comments").add(comment).await()

            // Send notification to event author
            val eventDoc = eventRef.get().await()
            val event = eventDoc.toObject(Event::class.java)
            
            if (event != null && event.authorUid != comment.authorUid) {
                val notification = AppNotification(
                    recipientUid = event.authorUid,
                    type = NotificationType.COMMENT,
                    title = "Nuevo comentario",
                    body = "${comment.authorName} comentó en tu evento: ${event.title}",
                    eventId = event.id,
                    createdAt = Timestamp.now()
                )
                notificationRepository.sendNotification(notification)
            }
        } catch (e: Exception) {
            Log.e("FirebaseCommentRepo", "Error adding comment", e)
        }
    }

    override suspend fun deleteComment(eventId: String, commentId: String) {
        try {
            firestore.collection("events")
                .document(eventId)
                .collection("comments")
                .document(commentId)
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e("FirebaseCommentRepo", "Error deleting comment", e)
        }
    }

    override suspend fun getCommentCount(eventId: String): Int {
        return try {
            val snapshot = firestore.collection("events")
                .document(eventId)
                .collection("comments")
                .get()
                .await()
            snapshot.size()
        } catch (e: Exception) {
            0
        }
    }
}
