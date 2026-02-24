package com.example.proyectofinaldisenomovil.data.model

import com.google.firebase.Timestamp
import java.security.Timestamp

/**
 * A user comment on a community event.
 *
 * Firestore collection : "events/{eventId}/comments"  (sub-collection)
 * Document ID          : auto-generated
 *
 * When a comment is created, a push notification must be sent to [Event.authorUid].
 */
data class Comment(
    val id: String = "",
    val eventId: String = "",
    val authorUid: String = "",
    /** Denormalized for fast rendering — synced when user updates their profile. */
    val authorName: String = "",
    val authorImageUrl: String? = null,
    val text: String = "",
    /**
     * Set to `true` by the AI content-moderation check before the comment
     * is persisted. Inappropriate comments are stored but hidden from the UI.
     */
    val isInappropriate: Boolean = false,
    val createdAt: Timestamp? = null,
) {
    /** True when the comment can be rendered in the public thread. */
    val isVisible: Boolean get() = !isInappropriate
}
