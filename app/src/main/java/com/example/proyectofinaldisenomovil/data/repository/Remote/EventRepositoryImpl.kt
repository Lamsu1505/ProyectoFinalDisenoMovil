package com.example.proyectofinaldisenomovil.data.repository.Remote

import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore // Se inyecta una instancia de FirebaseFirestore para interactuar con la base de datos
): EventRepository {
    override suspend fun fetchEvents(): List<Event> {
        TODO("Not yet implemented")
    }

    override fun observeFeedEvents(
        category: EventCategory?,
        latLng: Pair<Double, Double>?,
        radiusKm: Double
    ): Flow<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEventById(id: String): Event? {
        TODO("Not yet implemented")
    }

    override suspend fun updateEvent(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun verifyEvent(id: String, moderatorUid: String) {
        TODO("Not yet implemented")
    }

    override suspend fun rejectEvent(
        id: String,
        moderatorUid: String,
        reason: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun markResolved(id: String) {
        TODO("Not yet implemented")
    }

    override fun observePendingEvents(): Flow<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getVerifiedEvents(): List<Event> {
        TODO("Not yet implemented")
    }

    override suspend fun getEventsByIds(ids: List<String>): List<Event> {
        TODO("Not yet implemented")
    }

    override suspend fun createEvent(
        title: String,
        description: String,
        category: EventCategory,
        address: String,
        imageUrls: List<String>,
        startDate: Timestamp,
        endDate: Timestamp,
        maxAttendees: Int?
    ): Event {
        TODO("Not yet implemented")
    }

    override fun getAllEvents(): List<Event> {
        TODO("Not yet implemented")
    }

    override fun onEventAccept(event: Event) {
        TODO("Not yet implemented")
    }

    override fun onEventReject(
        event: Event,
        reason: String
    ) {
        TODO("Not yet implemented")
    }

    override fun editEvent(
        idEvent: String,
        newEvent: Event
    ) {
        TODO("Not yet implemented")
    }

}