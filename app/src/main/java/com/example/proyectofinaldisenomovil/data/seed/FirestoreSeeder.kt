package com.example.proyectofinaldisenomovil.data.seed

import android.util.Log
import com.example.proyectofinaldisenomovil.data.local.AppInitDataStore
import com.example.proyectofinaldisenomovil.domain.model.BadgeType
import com.example.proyectofinaldisenomovil.domain.model.Location
import com.example.proyectofinaldisenomovil.domain.model.User.User
import com.example.proyectofinaldisenomovil.domain.model.User.UserLevel
import com.example.proyectofinaldisenomovil.domain.model.User.UserRole
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.domain.model.Event.EventStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreSeeder @Inject constructor(
    private val db: FirebaseFirestore,
    private val appInitDataStore: AppInitDataStore,
) {
    suspend fun seedIfNeeded() {
        // Only seed once per device install.
        if (appInitDataStore.seedDoneFlow.first()) return

        try {
            seedUsers()
            seedEvent()
            appInitDataStore.setSeedDone(true)
            Log.i("FirestoreSeeder", "Seed inicial completado")
        } catch (e: Exception) {
            Log.e("FirestoreSeeder", "Fallo seed inicial", e)
        }
    }

    private suspend fun seedUsers() {
        val users = db.collection("users")

        val userDoc = users.document("seed_user_001")
        if (!userDoc.get().await().exists()) {
            val user = User(
                uid = userDoc.id,
                firstName = "Camilo",
                lastName = "Torres",
                email = "seed_user@vivetuzona.app",
                password = "",
                profileImageUrl = null,
                location = Location(latitude = 4.5333, longitude = -75.6833),
                city = "Armenia, Quindío",
                role = UserRole.USER,
                reputationPoints = 120,
                level = UserLevel.NOVATO,
                badges = listOf(BadgeType.FUNDADOR),
                isActive = true,
                createdAt = Timestamp.now(),
                rating = 4.5,
            )
            userDoc.set(userToMap(user)).await()
        }

        val modDoc = users.document("seed_mod_001")
        if (!modDoc.get().await().exists()) {
            val mod = User(
                uid = modDoc.id,
                firstName = "Juan",
                lastName = "Pérez",
                email = "seed_mod@vivetuzona.app",
                password = "",
                profileImageUrl = null,
                location = Location(latitude = 4.5333, longitude = -75.6833),
                city = "Armenia, Quindío",
                role = UserRole.MODERATOR,
                reputationPoints = 1000,
                level = UserLevel.ENTUSIASTA,
                badges = listOf(BadgeType.MODERATOR),
                isActive = true,
                createdAt = Timestamp.now(),
                rating = 5.0,
            )
            modDoc.set(userToMap(mod)).await()
        }
    }

    private suspend fun seedEvent() {
        val events = db.collection("events")
        val eventDoc = events.document("seed_event_001")
        if (eventDoc.get().await().exists()) return

        val start = Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000L)
        val end = Date(start.time + 2 * 60 * 60 * 1000L)

        val event = Event(
            id = eventDoc.id,
            authorUid = "seed_user_001",
            authorName = "Camilo Torres",
            title = "Feria de emprendedores locales",
            description = "Evento semilla para que todos los usuarios vean contenido desde el inicio.",
            category = EventCategory.SOCIAL,
            imageUrls = listOf("https://images.unsplash.com/photo-1555529669-e69e7aa0ba9a?w=800"),
            latitude = 4.5339,
            longitude = -75.6811,
            address = "Plaza de Bolívar, Armenia, Quindío",
            startDate = Timestamp(start),
            endDate = Timestamp(end),
            maxAttendees = 100,
            currentAttendees = 0,
            status = EventStatus.VERIFIED,
            moderatorUid = "seed_mod_001",
            importantVotes = 0,
            isResolved = false,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now(),
        )

        eventDoc.set(eventToMap(event)).await()
    }

    private fun userToMap(user: User): Map<String, Any?> = mapOf(
        "uid" to user.uid,
        "firstName" to user.firstName,
        "lastName" to user.lastName,
        "email" to user.email,
        "profileImageUrl" to user.profileImageUrl,
        "location" to user.location?.let { mapOf("latitude" to it.latitude, "longitude" to it.longitude) },
        "city" to user.city,
        "role" to user.role.name,
        "reputationPoints" to user.reputationPoints,
        "level" to user.level.name,
        "badges" to user.badges.map { it.name },
        "isActive" to user.isActive,
        "rating" to user.rating,
        "createdAt" to (user.createdAt ?: Timestamp.now()),
        "updatedAt" to Timestamp.now(),
    )

    private fun eventToMap(event: Event): Map<String, Any?> = mapOf(
        "id" to event.id,
        "authorUid" to event.authorUid,
        "authorName" to event.authorName,
        "title" to event.title,
        "description" to event.description,
        "category" to event.category.name,
        "imageUrls" to event.imageUrls,
        "latitude" to event.latitude,
        "longitude" to event.longitude,
        "address" to event.address,
        "startDate" to event.startDate,
        "endDate" to event.endDate,
        "maxAttendees" to event.maxAttendees,
        "currentAttendees" to event.currentAttendees,
        "status" to event.status.name,
        "moderatorUid" to event.moderatorUid,
        "rejectionReason" to event.rejectionReason,
        "importantVotes" to event.importantVotes,
        "isResolved" to event.isResolved,
        "createdAt" to (event.createdAt ?: Timestamp.now()),
        "updatedAt" to Timestamp.now(),
    )
}

