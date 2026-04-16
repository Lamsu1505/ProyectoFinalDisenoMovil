package com.example.proyectofinaldisenomovil.domain.model.User

//import com.google.firebase.Timestamp
//import com.vivetuzona.data.model.UserLevel
//import com.vivetuzona.data.model.UserRole
import com.example.proyectofinaldisenomovil.domain.model.BadgeType
import com.example.proyectofinaldisenomovil.domain.model.Location
import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val profileImageUrl: String? = null,
    /** Latitude of the user's home / reference location */
    val location : Location?,
    val city: String = "",
    val role: UserRole = UserRole.USER,
    val reputationPoints: Int = 0,
    val level: UserLevel = UserLevel.ESPECTADOR,
    val badges: List<BadgeType> = emptyList(),
    /** Firebase Cloud Messaging device token — updated on every app launch */
    val fcmToken: String? = null,
    /** Soft-delete flag: false = account disabled but data preserved */
    val isActive: Boolean = true,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
) {
    /** Full display name convenience property */
    val fullName: String get() = "$firstName $lastName".trim()

    /** True when the user holds the MODERATOR role */
    val isModerator: Boolean get() = role == UserRole.MODERATOR

    /**
     * Returns the [UserLevel] the user would reach with the given [points],
     * useful for UI progress indicators.
     */
    fun nextLevel(): UserLevel? =
        UserLevel.entries.firstOrNull { it.minPoints > reputationPoints }

    /** Points still needed to reach the next level, or null if already max */
    fun pointsToNextLevel(): Int? =
        nextLevel()?.let { it.minPoints - reputationPoints }
}
