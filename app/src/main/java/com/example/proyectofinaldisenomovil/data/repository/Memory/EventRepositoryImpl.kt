package com.example.proyectofinaldisenomovil.data.repository.Memory

import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.domain.model.Event.EventStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.filter

@Singleton
class EventRepositoryImpl @Inject constructor(): EventRepository {
    private val _events = MutableStateFlow<List<Event>>(emptyList())
     val events : StateFlow<List<Event>> = _events.asStateFlow()

    init{
        _events.value = fetchEvents()
    }

     suspend fun save(event: Event) {
        _events.value += event
    }

     suspend fun findById(id: String): Event? {
        return _events.value.firstOrNull { it.id == id }
    }

     suspend fun getAll(): List<Event> {
        return _events.value
    }


    private fun fetchEvents(): List<Event>{
        return listOf(
            Event(
                id = "event_001",
                authorUid = "user_001",
                authorName = "Camilo Torres",
                title = "Ciclopaseo nocturno por Armenia",
                description = "Únete al ciclopaseo mensual que recorre los principales parques y avenidas de Armenia. Apto para todas las edades. Trae tu bici, casco y muchas ganas de rodar.",
                category = EventCategory.DEPORTES,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800",
                    "https://images.unsplash.com/photo-1541625602330-2277a4c46182?w=800"
                ),
                latitude = 4.5339,
                longitude = -75.6811,
                address = "Parque de la Vida, Armenia, Quindío",
                maxAttendees = 100,
                currentAttendees = 67,
                status = EventStatus.VERIFIED,
                importantVotes = 42,
                isResolved = false,
                startDate = null,
                endDate = null,
                createdAt = null,
                updatedAt = null
            ),

            Event(
                id = "event_002",
                authorUid = "user_002",
                authorName = "Laura Gómez",
                title = "Torneo relámpago de microfútbol",
                description = "Torneo de microfútbol masculino y femenino en la cancha sintética del barrio Centenario. Inscripciones abiertas para equipos de 5 jugadores. Premio para el campeón.",
                category = EventCategory.DEPORTES,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1431324155629-1a6deb1dec8d?w=800"
                ),
                latitude = 4.5412,
                longitude = -75.6743,
                address = "Cancha Sintética Centenario, Armenia, Quindío",
                maxAttendees = 80,
                currentAttendees = 80,
                status = EventStatus.VERIFIED,
                importantVotes = 31,
                isResolved = false,
                startDate = null,
                endDate = null,
                createdAt = null,
                updatedAt = null
            ),

            Event(
                id = "event_003",
                authorUid = "user_003",
                authorName = "Sebastián Ríos",
                title = "Caminata ecológica Parque Miracielos",
                description = "Recorrido guiado de 8 km por senderos naturales del Parque Nacional del Café. Nivel de dificultad: moderado. Incluye avistamiento de aves y parada en mirador.",
                category = EventCategory.DEPORTES,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1551632811-561732d1e306?w=800",
                    "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=800"
                ),
                latitude = 4.4885,
                longitude = -75.7123,
                address = "Parque Nacional del Café, Montenegro, Quindío",
                maxAttendees = 30,
                currentAttendees = 22,
                status = EventStatus.VERIFIED,
                importantVotes = 19,
                isResolved = false,
                startDate = null,
                endDate = null,
                createdAt = null,
                updatedAt = null
            ),

            // ─── CULTURA ─────────────────────────────────────────────────────────────
            Event(
                id = "event_004",
                authorUid = "user_004",
                authorName = "Valentina Ospina",
                title = "Concierto de jazz en el Pasaje Uribe",
                description = "Noche de jazz en vivo con tres agrupaciones locales. Entrada libre. Trae tu silla o disfruta de pie. Habrá food trucks y artesanías. Apto para toda la familia.",
                category = EventCategory.CULTURA,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=800",
                    "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=800"
                ),
                latitude = 4.5367,
                longitude = -75.6792,
                address = "Pasaje Uribe, Centro, Armenia, Quindío",
                maxAttendees = null,
                currentAttendees = 134,
                status = EventStatus.VERIFIED,
                importantVotes = 88,
                isResolved = false,
                startDate = null,
                endDate = null,
                createdAt = null,
                updatedAt = null
            ),
            Event(
                id = "event_012",
                authorUid = "user_012",
                authorName = "Natalia Duque",
                title = "Feria de emprendedores locales",
                description = "Más de 50 emprendedores del Quindío exhibirán productos artesanales, gastronómicos y de moda. Habrá shows musicales, zona de comidas y actividades para niños. Entrada libre.",
                category = EventCategory.SOCIAL,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1555529669-e69e7aa0ba9a?w=800",
                    "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=800"
                ),
                latitude = 4.5378,
                longitude = -75.6820,
                address = "Plaza de Bolívar, Armenia, Quindío",
                maxAttendees = null,
                currentAttendees = 312,
                status = EventStatus.VERIFIED,
                importantVotes = 156,
                isResolved = false,
                startDate = null,
                endDate = null,
                createdAt = null,
                updatedAt = null
            ),

            Event(
                id = "event_013",
                authorUid = "user_013",
                authorName = "David Quintero",
                title = "Bazar navideño barrio El Bosque",
                description = "Bazar comunitario con venta de comidas típicas, artesanías navideñas, ropa y más. Las ganancias apoyan el alumbrado navideño del barrio. ¡Ven en familia!",
                category = EventCategory.SOCIAL,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1513885535751-8b9238bd345a?w=800"
                ),
                latitude = 4.5445,
                longitude = -75.6703,
                address = "Parque El Bosque, Armenia, Quindío",
                maxAttendees = null,
                currentAttendees = 175,
                status = EventStatus.VERIFIED,
                importantVotes = 67,
                isResolved = false,
                startDate = null,
                endDate = null,
                createdAt = null,
                updatedAt = null
            ),

            Event(
                id = "event_014",
                authorUid = "user_014",
                authorName = "Sara Vargas",
                title = "Encuentro de colectivos juveniles del Quindío",
                description = "Espacio de diálogo y conexión entre colectivos culturales, ambientales y sociales del departamento. Networking, presentaciones y cocreación de proyectos comunes.",
                category = EventCategory.SOCIAL,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1529156069898-49953e39b3ac?w=800"
                ),
                latitude = 4.5290,
                longitude = -75.6840,
                address = "Centro Cultural Comfenalco, Armenia, Quindío",
                maxAttendees = 120,
                currentAttendees = 77,
                status = EventStatus.PENDING_REVIEW,
                importantVotes = 23,
                isResolved = false,
                startDate = null,
                endDate = null,
                createdAt = null,
                updatedAt = null
            ),

            // ─── EVENTO RESUELTO (para estadísticas) ────────────────────────────────
            Event(
                id = "event_015",
                authorUid = "user_001",
                authorName = "Camilo Torres",
                title = "Maratón 5K por la paz",
                description = "Carrera pedestre de 5 kilómetros por las principales vías de Armenia. Evento ya realizado con gran éxito y participación ciudadana.",
                category = EventCategory.DEPORTES,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1452626038306-9aae5e071dd3?w=800"
                ),
                latitude = 4.5339,
                longitude = -75.6811,
                address = "Avenida Bolívar, Armenia, Quindío",
                maxAttendees = 500,
                currentAttendees = 487,
                status = EventStatus.VERIFIED,
                importantVotes = 203,
                isResolved = true,
                startDate = null,
                endDate = null,
                createdAt = null,
                updatedAt = null
            )
        )
    }

    override fun observeFeedEvents(
        category: EventCategory?,
        latLng: Pair<Double, Double>?,
        radiusKm: Double
    ): Flow<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEventById(id: String): Event? {
        return events.value.firstOrNull{ it.id == id }
    }

    override suspend fun createEvent(event: Event): String {
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
       return _events.value.filter { it.status == EventStatus.VERIFIED }
    }
}