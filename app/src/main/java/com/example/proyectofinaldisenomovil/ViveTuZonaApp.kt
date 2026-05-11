package com.example.proyectofinaldisenomovil

import android.app.Application
import com.mapbox.maps.Mapbox
import com.mapbox.maps.MapboxOptions
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ViveTuZonaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeMapbox()
    }

    private fun initializeMapbox() {
        val accessToken = BuildConfig.MAPBOX_ACCESS_TOKEN
        if (accessToken.isNotEmpty()) {
            val options = MapboxOptions.builder()
                .accessToken(accessToken)
                .build()
            Mapbox.initialize(applicationContext, options)
        }
    }
}