package com.example.proyectofinaldisenomovil.data.model

/**
 * Defines the two roles a registered account can hold.
 *
 * - [USER]      → Regular community member; can create events, comment, vote, attend.
 * - [MODERATOR] → Platform admin; can verify/reject events and mark them as resolved.
 *                 Moderators are **pre-loaded** into Firestore; they cannot self-register.
 */
enum class UserRole {
    USER,
    MODERATOR,
}
