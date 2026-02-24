package com.example.proyectofinaldisenomovil.data.model

import com.google.firebase.Timestamp
import com.vivetuzona.data.model.ReputationReason
import java.security.Timestamp

/**
 * Immutable audit record of every reputation point change for a user.
 *
 * Firestore collection : "reputationLogs"
 * Document ID          : auto-generated
 *
 * [User.reputationPoints] is the running total; this collection provides
 * a transparent history the user can inspect.
 */
data class ReputationLog(
    val id: String = "",
    val uid: String = "",
    /** Point delta — positive for gains, negative for penalties. */
    val points: Int = 0,
    val reason: ReputationReason = ReputationReason.EVENT_CREATED,
    /** ID of the Event, Comment, or Vote that triggered this change. */
    val relatedEntityId: String? = null,
    val createdAt: Timestamp? = null,
)
