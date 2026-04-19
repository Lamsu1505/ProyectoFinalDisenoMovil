package com.example.proyectofinaldisenomovil.domain.model.User

/**
 * Gamification levels for the Community Events theme.
 *
 * A user's level is derived from their [User.reputationPoints]:
 *   0–99      → ESPECTADOR
 *   100–299   → NOVATO
 *   300–599   → PARTICIPANTE
 *   600–1199  → ENTUSIASTA
 *   1200–2499 → CREADOR
 *   2500–4999 → ORGANIZADOR
 *   5000+     → LIDER_COMUNITARIO
 *
 * @property label      Human-readable Spanish name displayed in the UI.
 * @property minPoints  Minimum reputation points required to reach this level.
 * @property emoji      Visual emoji representation for the UI.
 */
enum class UserLevel(val label: String, val minPoints: Int, val emoji: String) {
    ESPECTADOR("Espectador", 0, "👀"),
    NOVATO("Novato", 100, "🌱"),
    PARTICIPANTE("Participante", 300, "🎯"),
    ENTUSIASTA("Entusiasta", 600, "⭐"),
    CREADOR("Creador", 1200, "🎪"),
    ORGANIZADOR("Organizador", 2500, "🏆"),
    LIDER_COMUNITARIO("Líder Comunitario", 5000, "👑");

    companion object {
        /** Returns the correct [UserLevel] for the given [points] total. */
        fun fromPoints(points: Int): UserLevel =
            entries.filter { it.minPoints <= points }.maxByOrNull { it.minPoints }
                ?: ESPECTADOR
    }
}
