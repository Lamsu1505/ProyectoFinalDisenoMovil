package com.example.proyectofinaldisenomovil.domain.model.User

/**
 * Gamification levels for the Community Events theme.
 *
 * A user's level is derived from their [User.reputationPoints]:
 *   0–99   → ESPECTADOR
 *   100–499 → PARTICIPANTE
 *   500–1499 → ORGANIZADOR
 *   1500+   → LIDER_COMUNITARIO
 *
 * @property label      Human-readable Spanish name displayed in the UI.
 * @property minPoints  Minimum reputation points required to reach this level.
 */
enum class UserLevel(val label: String, val minPoints: Int) {
    ESPECTADOR("Espectador", 0),
    PARTICIPANTE("Participante", 100),
    ENTUSIASTA("Entusiasta", 200),
    CREADOR("Creador", 500),
    NOVATO("Novato", 50),
    ADMIN("Admin", 1000),
    ORGANIZADOR("Organizador", 500),
    LIDER_COMUNITARIO("Líder Comunitario", 1500);

    companion object {
        /** Returns the correct [UserLevel] for the given [points] total. */
        fun fromPoints(points: Int): UserLevel =
            entries.filter { it.minPoints <= points }.maxByOrNull { it.minPoints }
                ?: ESPECTADOR
    }
}
