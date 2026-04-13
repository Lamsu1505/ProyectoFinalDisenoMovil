package com.example.proyectofinaldisenomovil.data.BdProvisional

import com.example.proyectofinaldisenomovil.domain.model.Comment
import com.google.firebase.Timestamp
import java.util.Date

/**
 * Datos quemados de comentarios para desarrollo.
 */
object MockCommentData {

    private val baseTime = System.currentTimeMillis()

    val comments = listOf(
        // Comentarios para evt_001 (Ciclopaseo)
        Comment(
            id = "comment_001",
            eventId = "evt_001",
            authorUid = "user_002",
            authorName = "Laura Gómez",
            text = "¡Qué buena iniciativa! Voy a ir con toda la familia.",
            createdAt = Timestamp(Date(baseTime - 3600000))
        ),
        Comment(
            id = "comment_002",
            eventId = "evt_001",
            authorUid = "user_003",
            authorName = "Sebastián Ríos",
            text = "¿A qué hora es exactamente? ¿Necesito llevar luz para la bici?",
            createdAt = Timestamp(Date(baseTime - 7200000))
        ),
        Comment(
            id = "comment_003",
            eventId = "evt_001",
            authorUid = "user_001",
            authorName = "Camilo Torres",
            text = "Es a las 6 PM. Sí, recomienda llevar luz frontal por seguridad.",
            createdAt = Timestamp(Date(baseTime - 5400000))
        ),
        
        // Comentarios para evt_004 (Jazz)
        Comment(
            id = "comment_004",
            eventId = "evt_004",
            authorUid = "user_005",
            authorName = "Daniela Salazar",
            text = "¡Amante del jazz! ¿Qué agrupaciones van a tocar?",
            createdAt = Timestamp(Date(baseTime - 1800000))
        ),
        Comment(
            id = "comment_005",
            eventId = "evt_004",
            authorUid = "user_004",
            authorName = "Valentina Ospina",
            text = "Van tres agrupaciones locales: Jazz Club, Cuarteto Azulado y Trío Café. ¡No te lo pierdas!",
            createdAt = Timestamp(Date(baseTime - 900000))
        ),
        Comment(
            id = "comment_006",
            eventId = "evt_004",
            authorUid = "user_006",
            authorName = "Andrés Castaño",
            text = "La mejor noche de jazz que he visto en Armenia.",
            createdAt = Timestamp(Date(baseTime - 2700000))
        ),

        // Comentarios para evt_010 (Limpieza Río)
        Comment(
            id = "comment_007",
            eventId = "evt_010",
            authorUid = "user_007",
            authorName = "Felipe Cardona",
            text = "¿Llevan guantes o debemos llevar los nuestros?",
            createdAt = Timestamp(Date(baseTime - 10800000))
        ),
        Comment(
            id = "comment_008",
            eventId = "evt_010",
            authorUid = "user_010",
            authorName = "Ricardo Montoya",
            text = "Nosotros proveemos guantes y bolsas. Solo trae ganas de ayudar y ropa que pueda ensuciarse.",
            createdAt = Timestamp(Date(baseTime - 9000000))
        )
    )

    fun getByEventId(eventId: String): List<Comment> =
        comments.filter { it.eventId == eventId }
            .sortedByDescending { it.createdAt }

    fun getCountByEventId(eventId: String): Int =
        comments.count { it.eventId == eventId }
}