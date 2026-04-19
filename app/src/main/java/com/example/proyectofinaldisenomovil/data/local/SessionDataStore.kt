package com.example.proyectofinaldisenomovil.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.proyectofinaldisenomovil.domain.model.UserRole
import com.example.proyectofinaldisenomovil.domain.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val ROLE = stringPreferencesKey("role")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_LASTNAME = stringPreferencesKey("user_lastname")
        private val USER_CITY = stringPreferencesKey("user_city")
        private val REPUTATION_POINTS = intPreferencesKey("reputation_points")
        private val USER_LEVEL = stringPreferencesKey("user_level")
        private val USER_BADGES = stringPreferencesKey("user_badges")
        private val PROFILE_IMAGE_URL = stringPreferencesKey("profile_image_url")
    }

    val sessionFlow: Flow<UserSession?> = dataStore.data.map { preferences ->
        val userId = preferences[USER_ID]
        val role = preferences[ROLE]
        
        if (userId != null && role != null) {
            try {
                UserSession(
                    userId = userId,
                    role = UserRole.valueOf(role)
                )
            } catch (e: IllegalArgumentException) {
                null
            }
        } else {
            null
        }
    }

    val userEmailFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_EMAIL]
    }

    val userNameFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_NAME]
    }

    val userLastnameFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_LASTNAME]
    }

    val userCityFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_CITY]
    }

    val reputationPointsFlow: Flow<Int> = dataStore.data.map { preferences ->
        preferences[REPUTATION_POINTS] ?: 0
    }

    val userLevelFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_LEVEL] ?: "ESPECTADOR"
    }

    val userBadgesFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_BADGES] ?: "[]"
    }

    val profileImageUrlFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PROFILE_IMAGE_URL]
    }

    suspend fun saveSession(session: UserSession) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = session.userId
            preferences[ROLE] = session.role.name
        }
    }

    suspend fun saveFullUserData(
        userId: String,
        role: UserRole,
        email: String,
        firstName: String,
        lastName: String,
        city: String,
        reputationPoints: Int,
        userLevel: String,
        badges: String,
        profileImageUrl: String?
    ) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[ROLE] = role.name
            preferences[USER_EMAIL] = email
            preferences[USER_NAME] = firstName
            preferences[USER_LASTNAME] = lastName
            preferences[USER_CITY] = city
            preferences[REPUTATION_POINTS] = reputationPoints
            preferences[USER_LEVEL] = userLevel
            preferences[USER_BADGES] = badges
            profileImageUrl?.let { preferences[PROFILE_IMAGE_URL] = it }
        }
    }

    suspend fun updateReputation(points: Int, level: String) {
        dataStore.edit { preferences ->
            preferences[REPUTATION_POINTS] = points
            preferences[USER_LEVEL] = level
        }
    }

    suspend fun updateBadges(badges: String) {
        dataStore.edit { preferences ->
            preferences[USER_BADGES] = badges
        }
    }

    suspend fun updateProfile(
        firstName: String,
        lastName: String,
        city: String,
        profileImageUrl: String?
    ) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = firstName
            preferences[USER_LASTNAME] = lastName
            preferences[USER_CITY] = city
            profileImageUrl?.let { preferences[PROFILE_IMAGE_URL] = it }
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
