package com.example.proyectofinaldisenomovil.data.BdProvisional

import com.example.proyectofinaldisenomovil.domain.model.Vote
import com.google.firebase.Timestamp
import java.util.Date

/**
 * Datos quemados de votes ("Me importa") para desarrollo.
 */
object MockVoteData {

    val votes = listOf(
        Vote(id = "vote_evt001_user002", eventId = "evt_001", uid = "user_002", createdAt = Timestamp(Date())),
        Vote(id = "vote_evt001_user003", eventId = "evt_001", uid = "user_003", createdAt = Timestamp(Date())),
        Vote(id = "vote_evt001_user004", eventId = "evt_001", uid = "user_004", createdAt = Timestamp(Date())),
        Vote(id = "vote_evt004_user001", eventId = "evt_004", uid = "user_001", createdAt = Timestamp(Date())),
        Vote(id = "vote_evt004_user002", eventId = "evt_004", uid = "user_002", createdAt = Timestamp(Date())),
        Vote(id = "vote_evt010_user001", eventId = "evt_010", uid = "user_001", createdAt = Timestamp(Date())),
    )

    fun getByUserId(uid: String): List<Vote> =
        votes.filter { it.uid == uid }

    fun getByEventId(eventId: String): List<Vote> =
        votes.filter { it.eventId == eventId }

    fun hasUserVoted(userId: String, eventId: String): Boolean =
        votes.any { it.uid == userId && it.eventId == eventId }
}