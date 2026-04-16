package com.example.proyectofinaldisenomovil.data.repository

import com.example.proyectofinaldisenomovil.domain.model.User.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUserById(uid: String): User?

    suspend fun createUser(user: User)

    suspend fun updateUser(user: User)

    suspend fun deleteAccount(uid: String)

    fun observeUser(uid: String): Flow<User?>

    suspend fun saveFcmToken(uid: String, token: String)
}