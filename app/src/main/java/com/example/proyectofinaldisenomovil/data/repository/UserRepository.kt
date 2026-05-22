package com.example.proyectofinaldisenomovil.data.repository

import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.User.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUserById(uid: String): User?

    suspend fun createUser(user: User)

    suspend fun updateUser(user: User)

    suspend fun deleteAccount(uid: String)

    fun observeUser(uid: String): Flow<User?>

    suspend fun saveFcmToken(uid: String, token: String)

    suspend fun registerUser (
        user: User
    ): User?

    suspend fun validateCredentials(email: String, password: String) : User?
    suspend fun getAllUsers() : List<User>
    suspend fun resetPassword(email: String): Boolean
    suspend fun findUserByEmail(email: String): User?
    suspend fun save(user: User)
    suspend fun login(email: String, password: String): User?

    suspend fun getLoggedInUser(): User?
    fun logOut()
    suspend fun getUserEvents(uid: String): List<Event>?
    suspend fun addReputationPoints(uid: String, pointsToAdd: Int)
}