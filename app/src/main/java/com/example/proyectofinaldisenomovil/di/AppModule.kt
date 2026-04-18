package com.example.proyectofinaldisenomovil.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.proyectofinaldisenomovil.core.utils.ResourceProvider
import com.example.proyectofinaldisenomovil.core.utils.ResourceProviderImpl
import com.example.proyectofinaldisenomovil.data.local.SessionDataStore
import com.example.proyectofinaldisenomovil.data.local.SessionManager
import com.example.proyectofinaldisenomovil.data.local.SettingsDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideSessionDataStore(
        dataStore: DataStore<Preferences>
    ): SessionDataStore {
        return SessionDataStore(dataStore)
    }

    @Provides
    @Singleton
    fun provideSettingsDataStore(
        dataStore: DataStore<Preferences>
    ): SettingsDataStore {
        return SettingsDataStore(dataStore)
    }

    @Provides
    @Singleton
    fun provideSessionManager(
        @ApplicationContext context: Context
    ): SessionManager {
        return SessionManager(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ResourceModule {

    @Binds
    @Singleton
    abstract fun bindResourceProvider(
        resourceProviderImpl: ResourceProviderImpl
    ): ResourceProvider
}
