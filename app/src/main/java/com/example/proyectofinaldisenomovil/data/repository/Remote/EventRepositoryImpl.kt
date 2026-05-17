package com.example.proyectofinaldisenomovil.data.repository.Remote

import android.util.Log
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.domain.model.Event.EventStatus
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore, // Se inyecta una instancia de FirebaseFirestore para interactuar con la base de datos
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
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
        return getAllEvents()
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

        Log.i("Create event", "LLego al repo IMPL")

        try {

            val currentUser = auth.currentUser
                ?: throw Exception("Usuario no autenticado")

            // Obtener información del autor
            val author = userRepository.getUserById(currentUser.uid)
                ?: throw Exception("Autor no encontrado")

            // Crear referencia del documento
            val eventRef = firestore
                .collection("events")
                .document()

            val now = Timestamp.now()

            // Crear evento
            val event = Event(
                id = eventRef.id,
                authorUid = currentUser.uid,
                authorName = author.fullName,
                title = title,
                description = description,
                category = category,
                imageUrls = imageUrls,
                address = address,

                // temporal mientras integras geocoding
                latitude = 0.0,
                longitude = 0.0,

                startDate = startDate,
                endDate = endDate,
                maxAttendees = maxAttendees,

                currentAttendees = 0,
                importantVotes = 0,

                status = EventStatus.PENDING_REVIEW,

                createdAt = now,
                updatedAt = now
            )

            // Guardar en Firestore
            eventRef
                .set(event)
                .await()

            return event

        } catch (e: Exception) {

            Log.e(
                "CREATE_EVENT",
                e.message ?: "Error creando evento"
            )

            throw e
        }
    }



    override suspend fun getAllEvents(): List<Event> {
        return try {
            val snapshot = firestore
                .collection("events")
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->

                document.toObject(Event::class.java)
            }

        } catch (e: Exception) {

            Log.e(
                "GET_EVENTS",
                e.message ?: "Error obteniendo eventos"
            )

            emptyList()
        }
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