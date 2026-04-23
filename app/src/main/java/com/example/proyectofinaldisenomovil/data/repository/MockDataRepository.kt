package com.example.proyectofinaldisenomovil.data.repository

import android.util.Log
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
    
    private var _users = mutableListOf<User>()
    
    private var _events = mutableListOf<Event>()
    
    private var _notifications = mutableListOf<AppNotification>()
    
    private var _likedEvents = mutableMapOf<String, MutableSet<String>>()
    
    private var _savedEvents = mutableMapOf<String, MutableSet<String>>()
    
    private var currentUser: User? = null
    
    const val MOCK_PASSWORD = "12345678"
    
    fun validateCredentials(email: String, password: String): User? {
        return null
    }
    
    fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): User? {
        return null
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
        return Event(id = "") // Stub
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
