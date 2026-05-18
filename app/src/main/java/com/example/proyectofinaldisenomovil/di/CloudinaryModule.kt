package com.example.proyectofinaldisenomovil.di

import com.cloudinary.android.MediaManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudinaryModule {
    @Provides
    @Singleton
    fun provideUploadPreset(): String {
        return "mobile-preset"
    }
}