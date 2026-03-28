package com.example.proyectofinaldisenomovil.domain.model

//import com.google.firebase.Timestamp
//import com.vivetuzona.data.model.NotificationType
import java.security.Timestamp

/**
 * In-app notification stored in Firestore for the notification list screen.
 *
 * Firestore collection : "notifications"
 * Document ID          : auto-generated
 *
 * Each notification is also delivered via Firebase Cloud Messaging (FCM)
 * as a data-only push message. The FCM payload uses [title] and [body].
 */
data class AppNotification(
    val id: String = "",
    val recipientUid: String = "",
    val type: NotificationType = NotificationType.NEW_EVENT_NEARBY,
    val title: String = "",
    val body: String = "",
    /** Deep-link target — navigates to event detail when tapped. */
    val eventId: String? = null,
    val isRead: Boolean = false,
    val createdAt: Timestamp? = null,
)
