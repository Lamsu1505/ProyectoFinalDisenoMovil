package com.example.proyectofinaldisenomovil.data.repository.Remote

import android.util.Log
import com.example.proyectofinaldisenomovil.data.repository.NotificationRepository
import com.example.proyectofinaldisenomovil.domain.model.AppNotification
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseNotificationRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : NotificationRepository {

    private val collection = firestore.collection("notifications")

    override fun observeNotifications(uid: String): Flow<List<AppNotification>> = callbackFlow {
        val subscription = collection
            .whereEqualTo("recipientUid", uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirebaseNotifRepo", "Error observing notifications", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val notifications = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(AppNotification::class.java)?.copy(id = doc.id)
                    }
                    trySend(notifications)
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun getUnreadCount(uid: String): Int {
        return try {
            val snapshot = collection
                .whereEqualTo("recipientUid", uid)
                .whereEqualTo("read", false)
                .get()
                .await()
            snapshot.size()
        } catch (e: Exception) {
            Log.e("FirebaseNotifRepo", "Error getting unread count", e)
            0
        }
    }

    override suspend fun markAsRead(notificationId: String) {
        try {
            collection.document(notificationId).update("read", true).await()
        } catch (e: Exception) {
            Log.e("FirebaseNotifRepo", "Error marking as read", e)
        }
    }

    override suspend fun markAllAsRead(uid: String) {
        try {
            val unread = collection
                .whereEqualTo("recipientUid", uid)
                .whereEqualTo("read", false)
                .get()
                .await()

            val batch = firestore.batch()
            for (doc in unread.documents) {
                batch.update(doc.reference, "read", true)
            }
            batch.commit().await()
        } catch (e: Exception) {
            Log.e("FirebaseNotifRepo", "Error marking all as read", e)
        }
    }

    override suspend fun sendNotification(notification: AppNotification) {
        try {
            collection.add(notification).await()
        } catch (e: Exception) {
            Log.e("FirebaseNotifRepo", "Error sending notification", e)
        }
    }
}
