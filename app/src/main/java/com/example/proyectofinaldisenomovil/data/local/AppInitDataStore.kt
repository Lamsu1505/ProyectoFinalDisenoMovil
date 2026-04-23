package com.example.proyectofinaldisenomovil.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInitDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        private val SEED_DONE = booleanPreferencesKey("firestore_seed_done")
    }

    val seedDoneFlow: Flow<Boolean> = dataStore.data.map { prefs -> prefs[SEED_DONE] ?: false }

    suspend fun setSeedDone(done: Boolean) {
        dataStore.edit { it[SEED_DONE] = done }
    }
}

