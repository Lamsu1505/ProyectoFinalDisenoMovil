package com.example.proyectofinaldisenomovil.domain.usecase

import com.example.proyectofinaldisenomovil.data.model.Event.Event
import com.example.proyectofinaldisenomovil.data.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Returns the public event feed filtered by [category] and geographic radius.
 *
 * The repository handles the Firestore geohash query; this use case is
 * a thin orchestrator that validates input and documents the business intent.
 */
class GetFeedEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository,
) {
    operator fun invoke(
        category: EventCategory? = null,
        userLatitude: Double? = null,
        userLongitude: Double? = null,
        radiusKm: Double = 10.0,
    ): Flow<List<Event>> {
        val latLng = if (userLatitude != null && userLongitude != null)
            Pair(userLatitude, userLongitude) else null

        return eventRepository.observeFeedEvents(
            category  = category,
            latLng    = latLng,
            radiusKm  = radiusKm,
        )
    }
}
