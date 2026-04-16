package com.example.proyectofinaldisenomovil.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.proyectofinaldisenomovil.domain.model.UserRole
import com.example.proyectofinaldisenomovil.domain.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

@Singleton
class SessionDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val ROLE = stringPreferencesKey("role")
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

    suspend fun saveSession(session: UserSession) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = session.userId
            preferences[ROLE] = session.role.name
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
