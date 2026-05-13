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
    fun provideCloudinaryConfig(@ApplicationContext context: Context): Map<String, String> {
        val config = mapOf(
            "cloud_name" to "dfbiw4xli",
            "api_key" to "184923461485653",
            "api_secret" to "eNsQdWwaDeAZuypvUxRs1SbkDr0"
        )

        // NOTA DE SEGURIDAD: En aplicaciones móviles, es peligroso incluir el 'api_secret'.
        // Se recomienda usar "Unsigned Uploads" con un "Upload Preset".
        
        // TODO: Para máxima seguridad, ve a la consola de Cloudinary -> Settings -> Upload -> Upload Presets
        // 1. Crea un nuevo preset.
        // 2. En 'Signing Mode', elige 'Unsigned'.
        // 3. Copia el nombre del preset aquí abajo (reemplazando "tu_upload_preset").
        val uploadPreset = "mobile-preset"

        // Inicialización obligatoria del MediaManager de Cloudinary
        try {
            MediaManager.init(context, config)
        } catch (e: Exception) {
            // Ya inicializado o error
        }

        return config
    }
}