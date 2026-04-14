package com.example.proyectofinaldisenomovil.data.repository

import com.example.proyectofinaldisenomovil.domain.model.AppNotification
import com.example.proyectofinaldisenomovil.domain.model.BadgeType
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.domain.model.Event.EventStatus
import com.example.proyectofinaldisenomovil.domain.model.Location
import com.example.proyectofinaldisenomovil.domain.model.NotificationType
import com.example.proyectofinaldisenomovil.domain.model.User.User
import com.example.proyectofinaldisenomovil.domain.model.User.UserLevel
import com.example.proyectofinaldisenomovil.domain.model.User.UserRole
import com.google.firebase.Timestamp
import java.util.Date
import java.util.UUID

object MockDataRepository {
    
    private val defaultLocation = Location(latitude = 4.5333, longitude = -75.6833)
    
    private var _users = mutableListOf(
        User(
            uid = "user_001",
            firstName = "Camilo",
            lastName = "Torres",
            email = "camilo@example.com",
            profileImageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
            location = defaultLocation,
            city = "Armenia, Quindío",
            role = UserRole.USER,
            reputationPoints = 450,
            level = UserLevel.CREADOR,
            badges = listOf(BadgeType.FOUNDER, BadgeType.EARLY_ADOPTER),
            isActive = true
        ),
        User(
            uid = "user_002",
            firstName = "Laura",
            lastName = "Gómez",
            email = "laura@example.com",
            profileImageUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200",
            location = defaultLocation,
            city = "Armenia, Quindío",
            role = UserRole.USER,
            reputationPoints = 280,
            level = UserLevel.ORGANIZADOR,
            badges = listOf(BadgeType.SOCIAL_BUTTERFLY),
            isActive = true
        ),
        User(
            uid = "user_003",
            firstName = "Sebastián",
            lastName = "Ríos",
            email = "sebastian@example.com",
            profileImageUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
            location = Location(latitude = 4.5667, longitude = -75.7500),
            city = "Montenegro, Quindío",
            role = UserRole.USER,
            reputationPoints = 120,
            level = UserLevel.ESPECTADOR,
            badges = emptyList(),
            isActive = true
        ),
        User(
            uid = "user_004",
            firstName = "Valentina",
            lastName = "Ospina",
            email = "valentina@example.com",
            profileImageUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200",
            location = defaultLocation,
            city = "Armenia, Quindío",
            role = UserRole.USER,
            reputationPoints = 520,
            level = UserLevel.CREADOR,
            badges = listOf(BadgeType.CULTURE_VIP),
            isActive = true
        ),
        User(
            uid = "mod_001",
            firstName = "Juan",
            lastName = "Pérez",
            email = "moderator1@example.com",
            profileImageUrl = "https://images.unsplash.com/photo-1560250097-0b93528c311a?w=200",
            location = defaultLocation,
            city = "Armenia, Quindío",
            role = UserRole.MODERATOR,
            reputationPoints = 1000,
            level = UserLevel.ADMIN,
            badges = listOf(BadgeType.MODERATOR),
            isActive = true
        ),
        User(
            uid = "mod_002",
            firstName = "María",
            lastName = "López",
            email = "moderator2@example.com",
            profileImageUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=200",
            location = defaultLocation,
            city = "Armenia, Quindío",
            role = UserRole.MODERATOR,
            reputationPoints = 1000,
            level = UserLevel.ADMIN,
            badges = listOf(BadgeType.MODERATOR),
            isActive = true
        )
    )
    
    private var _events = mutableListOf(
        Event(
            id = "event_001",
            authorUid = "user_001",
            authorName = "Camilo Torres",
            title = "Concierto de Rock en el Parque",
            description = "Disfruta de una noche llena de música y energía con las mejores bandas de rock locales. Habrá food trucks, zonas de descanso y un mercado de artesanías.",
            category = EventCategory.CULTURA,
            startDate = Timestamp(Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)),
            endDate = Timestamp(Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000 + 3 * 60 * 60 * 1000)),
            address = "Parque Central, Av. Principal 123, Armenia",
            imageUrls = listOf("https://images.unsplash.com/photo-1470229722913-7c0e2dbbafd3?w=800"),
            currentAttendees = 45,
            maxAttendees = 200,
            status = EventStatus.VERIFIED
        ),
        Event(
            id = "event_002",
            authorUid = "user_002",
            authorName = "Laura Gómez",
            title = "Maratón Ciudad 2026",
            description = "Únete a la carrera más grande del año. Categorías para todos los niveles. Incluye premiación y hidratación.",
            category = EventCategory.DEPORTES,
            startDate = Timestamp(Date(System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000)),
            endDate = Timestamp(Date(System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000 + 4 * 60 * 60 * 1000)),
            address = "Estadio Municipal, Calle Sport 456, Armenia",
            imageUrls = listOf("https://images.unsplash.com/photo-1552674605-db6ffd4facb5?w=800"),
            currentAttendees = 120,
            maxAttendees = 500,
            status = EventStatus.VERIFIED
        ),
        Event(
            id = "event_003",
            authorUid = "user_003",
            authorName = "Sebastián Ríos",
            title = "Feria de Gastronomía Local",
            description = "Degusta los mejores platos de la región. Talleres de cocina y presentaciones en vivo.",
            category = EventCategory.SOCIAL,
            startDate = Timestamp(Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000)),
            endDate = Timestamp(Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000 + 6 * 60 * 60 * 1000)),
            address = "Plaza del Pueblo, Centro Histórico, Armenia",
            imageUrls = listOf("https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800"),
            currentAttendees = 80,
            maxAttendees = 150,
            status = EventStatus.PENDING_REVIEW
        ),
        Event(
            id = "event_004",
            authorUid = "user_004",
            authorName = "Valentina Ospina",
            title = "Conferencia de Tecnología",
            description = "Expertos tecnológicos hablando sobre IA, blockchain y el futuro. Networking y talleres prácticos.",
            category = EventCategory.DIVERSION,
            startDate = Timestamp(Date(System.currentTimeMillis() + 21 * 24 * 60 * 60 * 1000)),
            endDate = Timestamp(Date(System.currentTimeMillis() + 21 * 24 * 60 * 60 * 1000 + 8 * 60 * 60 * 1000)),
            address = "Centro de Convenciones, Torre Innovation, Armenia",
            imageUrls = listOf("https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=800"),
            currentAttendees = 200,
            maxAttendees = 300,
            status = EventStatus.VERIFIED
        ),
        Event(
            id = "event_005",
            authorUid = "user_001",
            authorName = "Camilo Torres",
            title = "Exposición de Arte Moderno",
            description = "Obras de artistas emergentes y reconocidos. Inauguración con vino de honor.",
            category = EventCategory.CULTURA,
            startDate = Timestamp(Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000)),
            endDate = Timestamp(Date(System.currentTimeMillis() + 12 * 24 * 60 * 60 * 1000)),
            address = "Museo de Arte Contemporáneo, Av. Cultura 789, Armenia",
            imageUrls = listOf("https://images.unsplash.com/photo-1531243269054-5ebf6f34081e?w=800"),
            currentAttendees = 35,
            maxAttendees = 100,
            status = EventStatus.PENDING_REVIEW
        ),
        Event(
            id = "event_006",
            authorUid = "user_002",
            authorName = "Laura Gómez",
            title = "Torneo de Fútbol Amateur",
            description = "Participa en el torneo de fútbol más grande del Quindío. Equipos de todas las edades.",
            category = EventCategory.DEPORTES,
            startDate = Timestamp(Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000)),
            endDate = Timestamp(Date(System.currentTimeMillis() + 12 * 24 * 60 * 60 * 1000)),
            address = "Cancha Sintética Los Angeles, Armenia",
            imageUrls = listOf("https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=800"),
            currentAttendees = 150,
            maxAttendees = 200,
            status = EventStatus.PENDING_REVIEW
        )
    )
    
    private var _notifications = mutableListOf(
        AppNotification(
            id = "notif_001",
            recipientUid = "user_001",
            type = NotificationType.VERIFIED,
            title = "Evento aprobado",
            body = "Tu evento 'Concierto de Rock' ha sido aprobado.",
            eventId = "event_001",
            isRead = false,
            createdAt = Timestamp.now()
        ),
        AppNotification(
            id = "notif_002",
            recipientUid = "user_001",
            type = NotificationType.COMMENT,
            title = "Nuevo comentario",
            body = "Laura Gómez comentó en tu evento",
            eventId = "event_001",
            isRead = true,
            createdAt = Timestamp(Date(System.currentTimeMillis() - 86400000))
        ),
        AppNotification(
            id = "notif_003",
            recipientUid = "user_001",
            type = NotificationType.NEW_EVENT,
            title = "Nuevo asistente",
            body = "Sebastián Ríos confirmó asistencia",
            eventId = "event_001",
            isRead = false,
            createdAt = Timestamp(Date(System.currentTimeMillis() - 172800000))
        ),
        AppNotification(
            id = "notif_004",
            recipientUid = "mod_001",
            type = NotificationType.NEW_EVENT,
            title = "Nuevo evento pendiente",
            body = "Hay 3 eventos esperando moderación",
            eventId = null,
            isRead = false,
            createdAt = Timestamp(Date(System.currentTimeMillis() - 3600000))
        )
    )
    
    private var _likedEvents = mutableMapOf(
        "user_001" to mutableSetOf("event_002", "event_004"),
        "user_002" to mutableSetOf("event_001"),
        "user_003" to mutableSetOf("event_001", "event_004")
    )
    
    private var _savedEvents = mutableMapOf(
        "user_001" to mutableSetOf("event_003"),
        "user_002" to mutableSetOf("event_001", "event_005"),
        "user_003" to mutableSetOf("event_002")
    )
    
    private var currentUser: User? = null
    
    const val MOCK_PASSWORD = "password123"
    
    fun validateCredentials(email: String, password: String): User? {
        if (password != MOCK_PASSWORD) return null
        return _users.firstOrNull { it.email.equals(email, ignoreCase = true) }
    }
    
    fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): User? {
        if (_users.any { it.email.equals(email, ignoreCase = true) }) {
            return null
        }
        val newUser = User(
            uid = "user_${UUID.randomUUID().toString().take(8)}",
            firstName = firstName,
            lastName = lastName,
            email = email,
            location = defaultLocation,
            city = "Armenia, Quindío",
            role = UserRole.USER,
            reputationPoints = 0,
            level = UserLevel.ESPECTADOR,
            badges = emptyList(),
            isActive = true,
            createdAt = Timestamp.now()
        )
        _users.add(newUser)
        _likedEvents[newUser.uid] = mutableSetOf()
        _savedEvents[newUser.uid] = mutableSetOf()
        return newUser
    }
    
    fun getLoggedInUser(): User? = currentUser
    
    fun setLoggedInUser(user: User?) {
        currentUser = user
    }
    
    fun logout() {
        currentUser = null
    }
    
    fun getAllUsers(): List<User> = _users.toList()
    
    fun getUserById(uid: String): User? = _users.firstOrNull { it.uid == uid }
    
    fun getUserByEmail(email: String): User? = _users.firstOrNull { it.email.equals(email, ignoreCase = true) }
    
    fun updateUser(updatedUser: User) {
        val index = _users.indexOfFirst { it.uid == updatedUser.uid }
        if (index != -1) {
            _users[index] = updatedUser
            if (currentUser?.uid == updatedUser.uid) {
                currentUser = updatedUser
            }
        }
    }
    
    fun getAllEvents(): List<Event> = _events.toList()

    fun printEvents()
    {
        _events.forEach { println(it.title) }
    }

    
    fun getEventById(eventId: String): Event? = _events.firstOrNull { it.id == eventId }
    
    fun getVerifiedEvents(): List<Event> = _events.filter { it.status == EventStatus.VERIFIED }
    
    fun getPendingEvents(): List<Event> = _events.filter { it.status == EventStatus.PENDING_REVIEW }
    
    fun createEvent(
        title: String,
        description: String,
        category: EventCategory,
        address: String,
        imageUrls: List<String>,
        startDate: Timestamp,
        endDate: Timestamp,
        maxAttendees: Int?
    ): Event {
        val newEvent = Event(
            id = "event_${UUID.randomUUID().toString().take(8)}",
            authorUid = currentUser?.uid ?: "",
            authorName = currentUser?.fullName ?: "",
            title = title,
            description = description,
            category = category,
            address = address,
            imageUrls = imageUrls,
            startDate = startDate,
            endDate = endDate,
            maxAttendees = maxAttendees,
            currentAttendees = 0,
            status = EventStatus.PENDING_REVIEW,
            createdAt = Timestamp.now()
        )
        _events.add(newEvent)
        
        getModerators().forEach { mod ->
            _notifications.add(
                AppNotification(
                    id = "notif_${UUID.randomUUID().toString().take(8)}",
                    recipientUid = mod.uid,
                    type = NotificationType.NEW_EVENT,
                    title = "Nuevo evento pendiente",
                    body = "Evento '${newEvent.title}' espera moderación",
                    eventId = newEvent.id,
                    isRead = false,
                    createdAt = Timestamp.now()
                )
            )
        }
        
        return newEvent
    }
    
    fun updateEvent(updatedEvent: Event) {
        val index = _events.indexOfFirst { it.id == updatedEvent.id }
        if (index != -1) {
            _events[index] = updatedEvent.copy(updatedAt = Timestamp.now())
        }
    }
    
    fun deleteEvent(eventId: String) {
        _events.removeIf { it.id == eventId }
    }
    
    fun approveEvent(eventId: String) {
        val index = _events.indexOfFirst { it.id == eventId }
        if (index != -1) {
            val event = _events[index]
            _events[index] = event.copy(
                status = EventStatus.VERIFIED,
                moderatorUid = currentUser?.uid,
                updatedAt = Timestamp.now()
            )
            
            _notifications.add(
                AppNotification(
                    id = "notif_${UUID.randomUUID().toString().take(8)}",
                    recipientUid = event.authorUid,
                    type = NotificationType.VERIFIED,
                    title = "Evento aprobado",
                    body = "Tu evento '${event.title}' ha sido aprobado",
                    eventId = eventId,
                    isRead = false,
                    createdAt = Timestamp.now()
                )
            )
        }
    }
    
    fun rejectEvent(eventId: String, reason: String = "") {
        val index = _events.indexOfFirst { it.id == eventId }
        if (index != -1) {
            val event = _events[index]
            _events[index] = event.copy(
                status = EventStatus.REJECTED,
                moderatorUid = currentUser?.uid,
                rejectionReason = reason,
                updatedAt = Timestamp.now()
            )
            
            _notifications.add(
                AppNotification(
                    id = "notif_${UUID.randomUUID().toString().take(8)}",
                    recipientUid = event.authorUid,
                    type = NotificationType.REJECTED,
                    title = "Evento rechazado",
                    body = "Tu evento '${event.title}' fue rechazado. $reason",
                    eventId = eventId,
                    isRead = false,
                    createdAt = Timestamp.now()
                )
            )
        }
    }
    
    fun getEventsByCreator(creatorId: String): List<Event> = 
        _events.filter { it.authorUid == creatorId }
    
    private fun getModerators(): List<User> = _users.filter { it.role == UserRole.MODERATOR }
    
    fun getAllNotifications(): List<AppNotification> = _notifications.toList()
    
    fun getNotificationsForUser(uid: String): List<AppNotification> = 
        _notifications.filter { it.recipientUid == uid }
            .sortedByDescending { it.createdAt }
    
    fun markNotificationAsRead(notificationId: String) {
        val index = _notifications.indexOfFirst { it.id == notificationId }
        if (index != -1) {
            _notifications[index] = _notifications[index].copy(isRead = true)
        }
    }
    
    fun markAllNotificationsAsRead(uid: String) {
        _notifications = _notifications.map { 
            if (it.recipientUid == uid && !it.isRead) it.copy(isRead = true) else it 
        }.toMutableList()
    }
    
    fun getUnreadNotificationCount(uid: String): Int = 
        _notifications.count { it.recipientUid == uid && !it.isRead }
    
    fun toggleLikeEvent(userId: String, eventId: String): Boolean {
        val userLikes = _likedEvents.getOrPut(userId) { mutableSetOf() }
        return if (userLikes.contains(eventId)) {
            userLikes.remove(eventId)
            false
        } else {
            userLikes.add(eventId)
            true
        }
    }
    
    fun isEventLiked(userId: String, eventId: String): Boolean =
        _likedEvents[userId]?.contains(eventId) == true
    
    fun getLikedEvents(userId: String): List<Event> {
        val likedIds = _likedEvents[userId] ?: emptySet()
        return _events.filter { likedIds.contains(it.id) }
    }
    
    fun toggleSaveEvent(userId: String, eventId: String): Boolean {
        val userSaves = _savedEvents.getOrPut(userId) { mutableSetOf() }
        return if (userSaves.contains(eventId)) {
            userSaves.remove(eventId)
            false
        } else {
            userSaves.add(eventId)
            true
        }
    }
    
    fun isEventSaved(userId: String, eventId: String): Boolean =
        _savedEvents[userId]?.contains(eventId) == true
    
    fun getSavedEvents(userId: String): List<Event> {
        val savedIds = _savedEvents[userId] ?: emptySet()
        return _events.filter { savedIds.contains(it.id) }
    }
    
    fun attendEvent(userId: String, eventId: String): Boolean {
        val index = _events.indexOfFirst { it.id == eventId }
        if (index != -1) {
            val event = _events[index]
            val maxAttendees = event.maxAttendees
            if (maxAttendees == null || event.currentAttendees < maxAttendees) {
                _events[index] = event.copy(
                    currentAttendees = event.currentAttendees + 1
                )
                return true
            }
        }
        return false
    }
    
    fun leaveEvent(userId: String, eventId: String): Boolean {
        val index = _events.indexOfFirst { it.id == eventId }
        if (index != -1) {
            val event = _events[index]
            if (event.currentAttendees > 0) {
                _events[index] = event.copy(
                    currentAttendees = event.currentAttendees - 1
                )
                return true
            }
        }
        return false
    }
    
    fun isUserAttending(userId: String, eventId: String): Boolean {
        return false
    }
}
