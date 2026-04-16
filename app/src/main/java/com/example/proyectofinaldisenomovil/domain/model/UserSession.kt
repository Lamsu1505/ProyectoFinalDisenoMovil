package com.example.proyectofinaldisenomovil.domain.model

enum class UserRole {
    USER,
    ADMIN
}

data class UserSession(
    val userId: String,
    val role: UserRole
)
