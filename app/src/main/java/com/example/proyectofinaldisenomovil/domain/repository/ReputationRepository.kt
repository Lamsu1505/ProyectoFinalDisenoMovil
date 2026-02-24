package com.example.proyectofinaldisenomovil.domain.repository

import com.example.proyectofinaldisenomovil.data.model.BadgeType
import com.example.proyectofinaldisenomovil.data.model.ReputationLog
import com.example.proyectofinaldisenomovil.data.model.ReputationReason
import com.example.proyectofinaldisenomovil.data.model.User.UserLevel
import kotlinx.coroutines.flow.Flow

/**
 * Contract for the gamification/reputation system.
 *
 * Implemented by [com.vivetuzona.data.repository.FirebaseReputationRepository].
 */
interface ReputationRepository {

    /**
     * Adds the points defined by [reason] to [User.reputationPoints] atomically,
     * records a [ReputationLog] entry, and calls [checkAndAwardBadges].
     */
    suspend fun addPoints(
        uid: String,
        reason: ReputationReason,
        relatedEntityId: String? = null,
    )

    /** Returns the correct [UserLevel] for the given cumulative [points]. */
    fun getUserLevel(points: Int): UserLevel

    /**
     * Evaluates badge conditions and writes newly earned [BadgeType] values
     * to [User.badges]. Only awards badges the user doesn't already have.
     */
    suspend fun checkAndAwardBadges(uid: String)

    /** Real-time stream of reputation history for the statistics screen. */
    fun observeReputationLogs(uid: String): Flow<List<ReputationLog>>

    /** Returns all badges currently held by [uid]. */
    suspend fun getUserBadges(uid: String): List<BadgeType>
}
