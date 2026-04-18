package com.example.proyectofinaldisenomovil.data.repository.Memory

import android.util.Log
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository.MOCK_PASSWORD
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.BadgeType
import com.example.proyectofinaldisenomovil.domain.model.Location
import com.example.proyectofinaldisenomovil.domain.model.User.User
import com.example.proyectofinaldisenomovil.domain.model.User.UserLevel
import com.example.proyectofinaldisenomovil.domain.model.User.UserRole
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.text.set

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    private val defaultLocation = Location(latitude = 4.5333, longitude = -75.6833)

    private var currentUser: User? = null
    val MOCK_PASSWORD = "12345678"

    private var _users = mutableListOf(
        User(
            uid = "user_001",
            firstName = "Camilo",
            lastName = "Torres",
            email = "a@g.com",
            password = MOCK_PASSWORD,
            profileImageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
            location = defaultLocation,
            city = "Armenia, Quindío",
            role = UserRole.USER,
            reputationPoints = 450,
            level = UserLevel.CREADOR,
            badges = listOf(BadgeType.FOUNDER, BadgeType.EARLY_ADOPTER),
            isActive = true,
            rating = 4.5
        ),
        User(
            uid = "user_002",
            firstName = "Laura",
            lastName = "Gómez",
            email = "laura@example.com",
            password = MOCK_PASSWORD,
            profileImageUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200",
            location = defaultLocation,
            city = "Armenia, Quindío",
            role = UserRole.USER,
            reputationPoints = 280,
            level = UserLevel.ORGANIZADOR,
            badges = listOf(BadgeType.SOCIAL_BUTTERFLY),
            isActive = true,
            rating = 4.7
        ),
        User(
            uid = "user_003",
            firstName = "Sebastián",
            lastName = "Ríos",
            email = "sebastian@example.com",
            password = MOCK_PASSWORD,
            profileImageUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
            location = Location(latitude = 4.5667, longitude = -75.7500),
            city = "Montenegro, Quindío",
            role = UserRole.USER,
            reputationPoints = 120,
            level = UserLevel.ESPECTADOR,
            badges = emptyList(),
            isActive = true,
            rating = 3.5
        ),
        User(
            uid = "mod_001",
            firstName = "Juan",
            lastName = "Pérez",
            email = "moderator1@example.com",
            password = MOCK_PASSWORD,
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
            email = "m@g.com",
            password = MOCK_PASSWORD,
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


    override suspend fun createUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(uid: String) {
        TODO("Not yet implemented")
    }

    override fun observeUser(uid: String): Flow<User?> {
        TODO("Not yet implemented")
    }

    override suspend fun saveFcmToken(uid: String, token: String) {
        TODO("Not yet implemented")
    }

    override fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) : User?
    {
        if (_users.any { it.email.equals(email, ignoreCase = true) }) {
            return null
        }
        val newUser = User(
            uid = "user_${UUID.randomUUID().toString().take(8)}",
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
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
        return newUser
    }

    override fun validateCredentials(
        email: String,
        password: String
    ): User? {
        return _users.firstOrNull { it.email == email && it.password == password }
    }

    override fun getAllUsers(): List<User> = _users.toList()

    override suspend fun getUserById(uid: String): User? = _users.firstOrNull { it.uid == uid }


    override suspend fun updateUser(user: User) {
        val index = _users.indexOfFirst { it.uid == user.uid }
        if (index != -1) {
            _users[index] = user
            if (currentUser?.uid == user.uid) {
                currentUser = user
            }
        }
    }

    override suspend fun resetPassword(email: String, newPassword: String): Boolean {
        val index = _users.indexOfFirst { it.email.equals(email, ignoreCase = true) }
        return if (index != -1) {
            val user = _users[index]
            val updatedUser = user.copy(password = newPassword)
            _users[index] = updatedUser
            if (currentUser?.uid == user.uid) {
                currentUser = updatedUser
            }
            true
        } else {
            false
        }
    }

    override fun findUserByEmail(email: String): User? {
        return _users.firstOrNull { it.email.equals(email, ignoreCase = true) }
    }
}