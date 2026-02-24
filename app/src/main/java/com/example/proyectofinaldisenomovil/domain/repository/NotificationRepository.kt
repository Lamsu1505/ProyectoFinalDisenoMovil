package com.vivetuzona.domain.repository

import com.vivetuzona.data.model.AppNotification
import kotlinx.coroutines.flow.Flow

/**
 * Contract for in-app notification persistence and FCM delivery.
 *
 * Implemented by [com.vivetuzona.data.repository.FirebaseNotificationRepository].
 */
interface NotificationRepository {

    /** Real-time stream of notifications for [uid], newest first. */
    fun observeNotifications(uid: String): Flow<List<AppNotification>>

    /** Returns the count of unread notifications for the badge indicator. */
    suspend fun getUnreadCount(uid: String): Int

    /** Marks a single notification as read. */
    suspend fun markAsRead(notificationId: String)

    /** Marks all notifications for [uid] as read (e.g., when opening the list). */
    suspend fun markAllAsRead(uid: String)

    /**
     * Stores the notification document in Firestore.
     * FCM delivery is handled by a Cloud Function that listens to this collection.
     */
    suspend fun sendNotification(notification: AppNotification)
}
