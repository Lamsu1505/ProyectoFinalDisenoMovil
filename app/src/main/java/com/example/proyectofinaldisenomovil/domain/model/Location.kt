package com.example.proyectofinaldisenomovil.domain.model

import kotlin.math.*

data class Location(
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        private const val EARTH_RADIUS_KM = 6371.0
    }

    fun distanceTo(other: Location): Double {
        val lat1Rad = Math.toRadians(latitude)
        val lat2Rad = Math.toRadians(other.latitude)
        val deltaLat = Math.toRadians(other.latitude - latitude)
        val deltaLon = Math.toRadians(other.longitude - longitude)

        val a = sin(deltaLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(deltaLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_KM * c
    }

    fun distanceTo(lat: Double, lon: Double): Double {
        return distanceTo(Location(lat, lon))
    }
}