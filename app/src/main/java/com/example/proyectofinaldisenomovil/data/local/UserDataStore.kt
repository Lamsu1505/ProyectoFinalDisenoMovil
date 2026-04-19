package com.example.proyectofinaldisenomovil.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USER_DATA = stringPreferencesKey("user_data")
        private val USER_BADGES = stringPreferencesKey("user_badges")
        private val USER_POINTS = stringPreferencesKey("user_points")
        private val USER_LEVEL = stringPreferencesKey("user_level")
    }

    val userDataFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_DATA]
    }

    val userBadgesFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_BADGES] ?: "[]"
    }

    val userPointsFlow: Flow<Int> = dataStore.data.map { preferences ->
        preferences[USER_POINTS]?.toIntOrNull() ?: 0
    }

    val userLevelFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_LEVEL] ?: "ESPECTADOR"
    }

    suspend fun saveUserData(userJson: String) {
        dataStore.edit { preferences ->
            preferences[USER_DATA] = userJson
        }
    }

    suspend fun saveUserBadges(badgesJson: String) {
        dataStore.edit { preferences ->
            preferences[USER_BADGES] = badgesJson
        }
    }

    suspend fun saveUserPoints(points: Int) {
        dataStore.edit { preferences ->
            preferences[USER_POINTS] = points.toString()
        }
    }

    suspend fun saveUserLevel(level: String) {
        dataStore.edit { preferences ->
            preferences[USER_LEVEL] = level
        }
    }

    suspend fun updateUserProfile(
        name: String,
        lastName: String,
        city: String,
        profileImageUrl: String?
    ) {
        dataStore.edit { preferences ->
            val currentData = preferences[USER_DATA] ?: "{}"
            preferences[USER_DATA] = currentData // Simplified - would need JSON parsing in real implementation
        }
    }

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.remove(USER_DATA)
            preferences.remove(USER_BADGES)
            preferences.remove(USER_POINTS)
            preferences.remove(USER_LEVEL)
        }
    }
}