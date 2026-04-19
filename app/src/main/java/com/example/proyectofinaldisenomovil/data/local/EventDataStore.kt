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
class EventDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val EVENTS_DATA = stringPreferencesKey("events_data")
        private val SAVED_EVENTS = stringPreferencesKey("saved_events")
        private val LIKED_EVENTS = stringPreferencesKey("liked_events")
        private val ATTENDED_EVENTS = stringPreferencesKey("attended_events")
    }

    val eventsDataFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[EVENTS_DATA] ?: "[]"
    }

    val savedEventsFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[SAVED_EVENTS] ?: "[]"
    }

    val likedEventsFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[LIKED_EVENTS] ?: "[]"
    }

    val attendedEventsFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[ATTENDED_EVENTS] ?: "[]"
    }

    suspend fun saveEvents(eventsJson: String) {
        dataStore.edit { preferences ->
            preferences[EVENTS_DATA] = eventsJson
        }
    }

    suspend fun saveSavedEvents(eventIdsJson: String) {
        dataStore.edit { preferences ->
            preferences[SAVED_EVENTS] = eventIdsJson
        }
    }

    suspend fun saveLikedEvents(eventIdsJson: String) {
        dataStore.edit { preferences ->
            preferences[LIKED_EVENTS] = eventIdsJson
        }
    }

    suspend fun saveAttendedEvents(eventIdsJson: String) {
        dataStore.edit { preferences ->
            preferences[ATTENDED_EVENTS] = eventIdsJson
        }
    }

    suspend fun addSavedEvent(eventId: String) {
        dataStore.edit { preferences ->
            val current = preferences[SAVED_EVENTS] ?: "[]"
            preferences[SAVED_EVENTS] = if (current == "[]") {
                "[\"$eventId\"]"
            } else {
                current.dropLast(1) + ",\"$eventId\"]"
            }
        }
    }

    suspend fun removeSavedEvent(eventId: String) {
        dataStore.edit { preferences ->
            val current = preferences[SAVED_EVENTS] ?: "[]"
            preferences[SAVED_EVENTS] = current.replace("\"$eventId\",", "").replace("\"$eventId\"", "")
        }
    }

    suspend fun toggleLikedEvent(eventId: String) {
        dataStore.edit { preferences ->
            val current = preferences[LIKED_EVENTS] ?: "[]"
            val isLiked = current.contains(eventId)
            preferences[LIKED_EVENTS] = if (isLiked) {
                current.replace("\"$eventId\",", "").replace("\"$eventId\"", "")
            } else {
                if (current == "[]") "[\"$eventId\"]" else current.dropLast(1) + ",\"$eventId\"]"
            }
        }
    }

    suspend fun clearEventsData() {
        dataStore.edit { preferences ->
            preferences.remove(EVENTS_DATA)
            preferences.remove(SAVED_EVENTS)
            preferences.remove(LIKED_EVENTS)
            preferences.remove(ATTENDED_EVENTS)
        }
    }
}