package com.example.proyectofinaldisenomovil.domain.model.User

/**
 * Gamification levels for the Community Events theme.
 *
 * A user's level is derived from their [User.reputationPoints].
 * In this implementation, the user levels up every 100 points.
 *
 * @property label      Human-readable Spanish name displayed in the UI.
 * @property minPoints  Minimum reputation points required to reach this level.
 * @property emoji      Visual emoji representation for the UI.
 */
enum class UserLevel(val label: String, val minPoints: Int, val emoji: String) {
    ESPECTADOR("Espectador", 0, "👀"),
    PARTICIPANTE("Participante", 200, "🏋🏼‍♂️"),
    ORGANIZADOR("Organizador", 500, "🧠"),
    LIDER_COMUNITARIO("Líder Comunitario", 1000, "👑");

    /** Returns the next level in the progression, or null if already at max. */
    fun nextLevel(): UserLevel? {
        val nextOrdinal = ordinal + 1
        return if (nextOrdinal < entries.size) entries[nextOrdinal] else null
    }

    companion object {
        /** Returns the correct [UserLevel] for the given [points] total. */
        fun fromPoints(points: Int): UserLevel =
            entries.filter { it.minPoints <= points }.maxByOrNull { it.minPoints }
                ?: ESPECTADOR
    }
}
