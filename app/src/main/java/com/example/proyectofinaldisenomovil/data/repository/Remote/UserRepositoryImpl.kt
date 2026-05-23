package com.example.proyectofinaldisenomovil.data.repository.Remote

import android.util.Log
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.BadgeType
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Location
import com.example.proyectofinaldisenomovil.domain.model.User.User
import com.example.proyectofinaldisenomovil.domain.model.User.UserLevel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): UserRepository {

    private val collection = firestore.collection("users")

    private val _users = MutableStateFlow<List<User>>(emptyList())
    private val users: StateFlow<List<User>> = _users.asStateFlow()

    init {
        collection.addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                _users.value = it.documents.mapNotNull { snap ->
                    snap.toObject(User::class.java)?.apply { uid = snap.id }
                }
            }
        }
    }

    override suspend fun save(user: User) {
        collection.add(user).await()
    }

    override suspend fun getUserById(uid: String): User? {
        val snapshot = collection.document(uid).get().await()
        return snapshot.toObject(User::class.java)?.apply { this.uid = snapshot.id }
    }

    override suspend fun login(email: String, password: String): User? {
        return try {
            val responseUser = auth.signInWithEmailAndPassword(email, password).await()
            val uid = responseUser.user?.uid ?: throw Exception("Usuario no encontrado")
            getUserById(uid)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e("LOGIN", "Contraseña incorrecta")
            null
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e("LOGIN", "Usuario no existe")
            null
        } catch (e: Exception) {
            Log.e("LOGIN", e.message ?: "Error")
            null
        }
    }

    override suspend fun getLoggedInUser(): User? {
        val uid = auth.currentUser?.uid ?: return null
        return getUserById(uid)
    }

    override fun logOut() {
        auth.signOut()
    }

    override suspend fun getUserEvents(uid: String): List<Event>? {
        return try {
            val snapshot = firestore.collection("events")
                .whereEqualTo("authorUid", uid)
                .get().await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(Event::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun createUser(user: User) {
        collection.document(user.uid).set(user).await()
    }

    override suspend fun updateUser(user: User) {
        collection.document(user.uid).set(user).await()
    }

    override suspend fun deleteAccount(uid: String) {
        collection.document(uid).delete().await()
    }

    override fun observeUser(uid: String): Flow<User?> {
        TODO("Not yet implemented")
    }

    override suspend fun saveFcmToken(uid: String, token: String) {
        collection.document(uid).update("fcmToken", token).await()
    }

    override suspend fun registerUser(user: User): User? {
        val newUser = auth.createUserWithEmailAndPassword(user.email, user.password).await()
        val uid = newUser.user?.uid ?: throw Exception("Error al obtener el UID")
        val userCopy = user.copy(
            uid = uid,
            password = "",
            createdAt = Timestamp.now()
        )
        collection.document(uid).set(userCopy).await()
        return userCopy
    }

    override suspend fun validateCredentials(email: String, password: String): User? {
        val snapshot = collection.whereEqualTo("email", email).whereEqualTo("password", password).get().await()
        return if (snapshot.isEmpty) null else snapshot.documents.first().toObject(User::class.java)?.apply { uid = snapshot.documents.first().id }
    }

    override suspend fun getAllUsers(): List<User> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(User::class.java)?.apply { uid = it.id } }
    }

    override suspend fun resetPassword(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun findUserByEmail(email: String): User? {
        val result = collection.whereEqualTo("email", email).get().await()
        return if (result.isEmpty) null else result.documents[0].toObject(User::class.java)
    }

    override suspend fun addReputationPoints(uid: String, pointsToAdd: Int) {
        try {
            val userRef = collection.document(uid)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userRef)
                val currentPoints = snapshot.getLong("reputationPoints")?.toInt() ?: 0
                val newPoints = (currentPoints + pointsToAdd).coerceAtLeast(0)
                val newLevel = UserLevel.fromPoints(newPoints)
                val nextLevel = newLevel.nextLevel()
                val pointsToNext = nextLevel?.let { it.minPoints - newPoints } ?: 0

                transaction.update(userRef, mapOf(
                    "reputationPoints" to newPoints,
                    "level" to newLevel.name,
                    "pointsToNextLevel" to pointsToNext
                ))
            }.await()
            checkAndAwardBadges(uid)
        } catch (e: Exception) {
            Log.e("USER_REPO", "Error updating reputation: ${e.message}")
        }
    }

    override suspend fun incrementVerifiedEvents(uid: String) {
        try {
            val userRef = collection.document(uid)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userRef)
                val count = snapshot.getLong("verifiedEventsCount")?.toInt() ?: 0
                transaction.update(userRef, "verifiedEventsCount", count + 1)
            }.await()
            checkAndAwardBadges(uid)
        } catch (e: Exception) {
            Log.e("USER_REPO", "Error incrementing verified events: ${e.message}")
        }
    }

    override suspend fun incrementTotalLikes(uid: String, increment: Int) {
        try {
            val userRef = collection.document(uid)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userRef)
                val likes = snapshot.getLong("totalLikesReceived")?.toInt() ?: 0
                transaction.update(userRef, "totalLikesReceived", (likes + increment).coerceAtLeast(0))
            }.await()
            checkAndAwardBadges(uid)
        } catch (e: Exception) {
            Log.e("USER_REPO", "Error incrementing total likes: ${e.message}")
        }
    }

    // Cambios aplicados en checkAndAwardBadges:
    override suspend fun checkAndAwardBadges(uid: String) {
        try {
            val userRef = collection.document(uid)
            val user = getUserById(uid) ?: return
            val newBadges = user.badges.toMutableList()

            // 1) Primera publicación verificada
            if (user.verifiedEventsCount >= 1 && !newBadges.contains(BadgeType.PRIMERA_PUBLICACION.name)) {
                newBadges.add(BadgeType.PRIMERA_PUBLICACION.name) // Corregido: .name y tipo de trofeo
            }
            // 2) 5 Publicaciones verificadas
            if (user.verifiedEventsCount >= 5 && !newBadges.contains(BadgeType.CINCO_PUBLICACIONES.name)) {
                newBadges.add(BadgeType.CINCO_PUBLICACIONES.name) // Corregido: .name
            }
            // 3) 20 publicaciones verificadas
            if (user.verifiedEventsCount >= 20 && !newBadges.contains(BadgeType.VEINTE_PUBLICACIONES.name)) {
                newBadges.add(BadgeType.VEINTE_PUBLICACIONES.name) // Corregido: .name
            }
            // 4) 50+ likes
            if (user.totalLikesReceived >= 50 && !newBadges.contains(BadgeType.CINCUENTA_LIKES.name)) {
                newBadges.add(BadgeType.CINCUENTA_LIKES.name) // Corregido: .name
            }
            // 5) 200+ likes
            if (user.totalLikesReceived >= 200 && !newBadges.contains(BadgeType.DOSCIENTOS_LIKES.name)) {
                newBadges.add(BadgeType.DOSCIENTOS_LIKES.name) // Corregido: .name
            }
            // 6) 1000+ likes
            if (user.totalLikesReceived >= 1000 && !newBadges.contains(BadgeType.MIL_LIKES.name)) {
                newBadges.add(BadgeType.MIL_LIKES.name) // Corregido: .name
            }
            // 7) 1+ Año de antigüedad
            user.createdAt?.let { createdAt ->
                val oneYearAgo = Calendar.getInstance().apply { add(Calendar.YEAR, -1) }.time
                if (createdAt.toDate().before(oneYearAgo) && !newBadges.contains(BadgeType.UN_ANO_ANTIGUEDAD.name)) {
                    newBadges.add(BadgeType.UN_ANO_ANTIGUEDAD.name) // Corregido: .name
                }
            }

            if (newBadges.size > user.badges.size) {
                userRef.update("badges", newBadges).await()
            }
        } catch (e: Exception) {
            Log.e("BADGES", "Error checking badges: ${e.message}")
        }
    }
}
