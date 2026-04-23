package com.example.proyectofinaldisenomovil.data.repository.firebase

import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.BadgeType
import com.example.proyectofinaldisenomovil.domain.model.Location
import com.example.proyectofinaldisenomovil.domain.model.User.User
import com.example.proyectofinaldisenomovil.domain.model.User.UserLevel
import com.example.proyectofinaldisenomovil.domain.model.User.UserRole
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreUserRepository @Inject constructor(
    private val db: FirebaseFirestore,
) : UserRepository {

    private val users get() = db.collection(COLLECTION_USERS)

    override suspend fun getUserById(uid: String): User? {
        val snap = users.document(uid).get().await()
        return if (snap.exists()) snap.toUser(uid) else null
    }

    override suspend fun createUser(user: User) {
        require(user.uid.isNotBlank()) { "User.uid is required" }
        users.document(user.uid).set(user.toFirestoreMap()).await()
    }

    override suspend fun updateUser(user: User) {
        require(user.uid.isNotBlank()) { "User.uid is required" }
        users.document(user.uid).set(user.toFirestoreMap()).await()
    }

    override suspend fun deleteAccount(uid: String) {
        // Soft-delete to preserve relational integrity
        users.document(uid).update(mapOf(FIELD_IS_ACTIVE to false, FIELD_UPDATED_AT to Timestamp.now())).await()
    }

    override fun observeUser(uid: String): Flow<User?> = flow {
        emit(getUserById(uid))
    }

    override suspend fun saveFcmToken(uid: String, token: String) {
        users.document(uid).update(mapOf(FIELD_FCM_TOKEN to token, FIELD_UPDATED_AT to Timestamp.now())).await()
    }

    // --- Legacy local-auth APIs (no longer used once FirebaseAuth is wired) ---
    override fun registerUser(firstName: String, lastName: String, email: String, password: String): User? = null
    override fun validateCredentials(email: String, password: String): User? = null
    override fun getAllUsers(): List<User> = emptyList()
    override suspend fun resetPassword(email: String, newPassword: String): Boolean = false
    override fun findUserByEmail(email: String): User? = null

    private fun com.google.firebase.firestore.DocumentSnapshot.toUser(uidFallback: String): User {
        val uid = getString(FIELD_UID) ?: uidFallback
        val badges = (get(FIELD_BADGES) as? List<*>)?.mapNotNull { raw ->
            (raw as? String)?.let { runCatching { BadgeType.valueOf(it) }.getOrNull() }
        } ?: emptyList()

        val level = (getString(FIELD_LEVEL) ?: UserLevel.ESPECTADOR.name).let {
            runCatching { UserLevel.valueOf(it) }.getOrDefault(UserLevel.ESPECTADOR)
        }

        val role = (getString(FIELD_ROLE) ?: UserRole.USER.name).let {
            runCatching { UserRole.valueOf(it) }.getOrDefault(UserRole.USER)
        }

        val location = (get(FIELD_LOCATION) as? Map<*, *>)?.let { m ->
            val lat = (m["latitude"] as? Number)?.toDouble()
            val lon = (m["longitude"] as? Number)?.toDouble()
            if (lat != null && lon != null) Location(latitude = lat, longitude = lon) else null
        }

        return User(
            uid = uid,
            firstName = getString(FIELD_FIRST_NAME) ?: "",
            lastName = getString(FIELD_LAST_NAME) ?: "",
            email = getString(FIELD_EMAIL) ?: "",
            password = "",
            profileImageUrl = getString(FIELD_PROFILE_IMAGE_URL),
            location = location,
            city = getString(FIELD_CITY) ?: "",
            role = role,
            reputationPoints = (getLong(FIELD_REPUTATION_POINTS) ?: 0L).toInt(),
            pointsToNextLevel = 0,
            level = level,
            badges = badges,
            fcmToken = getString(FIELD_FCM_TOKEN),
            isActive = getBoolean(FIELD_IS_ACTIVE) ?: true,
            createdAt = getTimestamp(FIELD_CREATED_AT),
            updatedAt = getTimestamp(FIELD_UPDATED_AT),
            rating = getDouble(FIELD_RATING),
        )
    }

    private fun User.toFirestoreMap(): Map<String, Any?> = mapOf(
        FIELD_UID to uid,
        FIELD_FIRST_NAME to firstName,
        FIELD_LAST_NAME to lastName,
        FIELD_EMAIL to email,
        FIELD_PROFILE_IMAGE_URL to profileImageUrl,
        FIELD_LOCATION to location?.let { mapOf("latitude" to it.latitude, "longitude" to it.longitude) },
        FIELD_CITY to city,
        FIELD_ROLE to role.name,
        FIELD_REPUTATION_POINTS to reputationPoints,
        FIELD_LEVEL to level.name,
        FIELD_BADGES to badges.map { it.name },
        FIELD_FCM_TOKEN to fcmToken,
        FIELD_IS_ACTIVE to isActive,
        FIELD_RATING to rating,
        FIELD_CREATED_AT to (createdAt ?: Timestamp.now()),
        FIELD_UPDATED_AT to Timestamp.now(),
    )

    private companion object {
        const val COLLECTION_USERS = "users"

        const val FIELD_UID = "uid"
        const val FIELD_FIRST_NAME = "firstName"
        const val FIELD_LAST_NAME = "lastName"
        const val FIELD_EMAIL = "email"
        const val FIELD_PROFILE_IMAGE_URL = "profileImageUrl"
        const val FIELD_LOCATION = "location"
        const val FIELD_CITY = "city"
        const val FIELD_ROLE = "role"
        const val FIELD_REPUTATION_POINTS = "reputationPoints"
        const val FIELD_LEVEL = "level"
        const val FIELD_BADGES = "badges"
        const val FIELD_FCM_TOKEN = "fcmToken"
        const val FIELD_IS_ACTIVE = "isActive"
        const val FIELD_RATING = "rating"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
    }
}

