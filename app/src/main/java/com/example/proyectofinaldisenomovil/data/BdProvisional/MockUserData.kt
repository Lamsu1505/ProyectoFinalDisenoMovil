//package com.example.proyectofinaldisenomovil.data.BdProvisional
//
//import com.example.proyectofinaldisenomovil.domain.model.BadgeType
//import com.example.proyectofinaldisenomovil.domain.model.Location
//import com.example.proyectofinaldisenomovil.domain.model.User.UserLevel
//import com.example.proyectofinaldisenomovil.domain.model.User.UserRole
//import com.example.proyectofinaldisenomovil.domain.model.User.User
//
///**
// * Datos quemados de usuarios para desarrollo (Fase 2).
// * Incluye usuarios regulares y moderadores para probar ambos flujos.
// *
// * Contraseña para todos los usuarios de prueba: "password123"
// */
//object MockUserData {
//
//    // Contraseña simulada (en realidad se validaría contra Firebase Auth)
//    const val MOCK_PASSWORD = "12345678"
//
//    // Location por defecto para Armenia, Quindío
//    private val defaultLocation = Location(latitude = 4.5333, longitude = -75.6833)
//
//    val users = listOf(
//        // ─── USUARIOS REGULARES ───────────────────────────────────────────────────
//        User(
//            uid = "user_001",
//            firstName = "Camilo",
//            lastName = "Torres",
//            email = "a@g.com",
//            profileImageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
//            location = defaultLocation,
//            city = "Armenia, Quindío",
//            role = UserRole.USER,
//            reputationPoints = 450,
//            level = UserLevel.CREADOR,
//            badges = listOf(BadgeType.FOUNDER, BadgeType.EARLY_ADOPTER),
//            isActive = true
//        ),
//        User(
//            uid = "user_002",
//            firstName = "Laura",
//            lastName = "Gómez",
//            email = "laura@example.com",
//            profileImageUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200",
//            location = defaultLocation,
//            city = "Armenia, Quindío",
//            role = UserRole.USER,
//            reputationPoints = 280,
//            level = UserLevel.ORGANIZADOR,
//            badges = listOf(BadgeType.SOCIAL_BUTTERFLY),
//            isActive = true
//        ),
//        User(
//            uid = "user_003",
//            firstName = "Sebastián",
//            lastName = "Ríos",
//            email = "sebastian@example.com",
//            profileImageUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
//            location = Location(latitude = 4.5667, longitude = -75.7500), // Montenegro
//            city = "Montenegro, Quindío",
//            role = UserRole.USER,
//            reputationPoints = 120,
//            level = UserLevel.ESPECTADOR,
//            badges = emptyList(),
//            isActive = true
//        ),
//        User(
//            uid = "user_004",
//            firstName = "Valentina",
//            lastName = "Ospina",
//            email = "valentina@example.com",
//            profileImageUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200",
//            location = defaultLocation,
//            city = "Armenia, Quindío",
//            role = UserRole.USER,
//            reputationPoints = 520,
//            level = UserLevel.CREADOR,
//            badges = listOf(BadgeType.CULTURE_VIP),
//            isActive = true
//        ),
//        User(
//            uid = "user_005",
//            firstName = "Daniela",
//            lastName = "Salazar",
//            email = "daniela@example.com",
//            profileImageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
//            location = defaultLocation,
//            city = "Armenia, Quindío",
//            role = UserRole.USER,
//            reputationPoints = 85,
//            level = UserLevel.ESPECTADOR,
//            badges = emptyList(),
//            isActive = true
//        ),
//        User(
//            uid = "user_006",
//            firstName = "Andrés",
//            lastName = "Castaño",
//            email = "andres@example.com",
//            profileImageUrl = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200",
//            location = defaultLocation,
//            city = "Armenia, Quindío",
//            role = UserRole.USER,
//            reputationPoints = 310,
//            level = UserLevel.ORGANIZADOR,
//            badges = listOf(BadgeType.VOLUNTEER),
//            isActive = true
//        ),
//        User(
//            uid = "user_007",
//            firstName = "Felipe",
//            lastName = "Cardona",
//            email = "felipe@example.com",
//            profileImageUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200",
//            location = defaultLocation,
//            city = "Armenia, Quindío",
//            role = UserRole.USER,
//            reputationPoints = 180,
//            level = UserLevel.ENTUSIASTA,
//            badges = emptyList(),
//            isActive = true
//        ),
//        User(
//            uid = "user_008",
//            firstName = "Mariana",
//            lastName = "Herrera",
//            email = "mariana@example.com",
//            profileImageUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200",
//            location = defaultLocation,
//            city = "Armenia, Quindío",
//            role = UserRole.USER,
//            reputationPoints = 95,
//            level = UserLevel.ESPECTADOR,
//            badges = emptyList(),
//            isActive = true
//        ),
//        User(
//            uid = "user_009",
//            firstName = "Juliana",
//            lastName = "Mejía",
//            email = "juliana@example.com",
//            profileImageUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200",
//            location = defaultLocation,
//            city = "Armenia, Quindío",
//            role = UserRole.USER,
//            reputationPoints = 42,
//            level = UserLevel.NOVATO,
//            badges = emptyList(),
//            isActive = true
//        ),
//        User(
//            uid = "user_010",
//            firstName = "Ricardo",
//            lastName = "Montoya",
//            email = "ricardo@example.com",
//            profileImageUrl = "https://images.unsplash.com/photo-1507591064344-4c6ce005b128?w=200",
//            location = defaultLocation,
//            city = "Armenia, Quindío",
//            role = UserRole.USER,
//            reputationPoints = 440,
//            level = UserLevel.CREADOR,
//            badges = listOf(BadgeType.VOLUNTEER),
//            isActive = true
//        ),
//
//        // ─── MODERADORES ───────────────────────────────────────────────────────────
//        User(
//            uid = "mod_001",
//            firstName = "Juan",
//            lastName = "Pérez",
//            email = "moderator1@example.com",
//            profileImageUrl = "https://images.unsplash.com/photo-1560250097-0b93528c311a?w=200",
//            location = defaultLocation,
//            city = "Armenia, Quindío",
//            role = UserRole.MODERATOR,
//            reputationPoints = 1000,
//            level = UserLevel.ADMIN,
//            badges = listOf(BadgeType.MODERATOR),
//            isActive = true
//        ),
//        User(
//            uid = "mod_002",
//            firstName = "María",
//            lastName = "López",
//            email = "moderator2@example.com",
//            profileImageUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=200",
//            location = defaultLocation,
//            city = "Armenia, Quindío",
//            role = UserRole.MODERATOR,
//            reputationPoints = 1000,
//            level = UserLevel.ADMIN,
//            badges = listOf(BadgeType.MODERATOR),
//            isActive = true
//        )
//    )
//
//    // ─── Helpers de búsqueda ─────────────────────────────────────────────────────
//
//    fun getByEmail(email: String): User? =
//        users.firstOrNull { it.email.equals(email, ignoreCase = true) }
//
//    fun getById(uid: String): User? =
//        users.firstOrNull { it.uid == uid }
//
//    fun getModerators(): List<User> =
//        users.filter { it.role == UserRole.MODERATOR }
//
//    fun getRegularUsers(): List<User> =
//        users.filter { it.role == UserRole.USER }
//
//    /**
//     * Valida credenciales (simulado - en producción sería Firebase Auth)
//     * @return User si las credenciales son válidas, null si no
//     */
//    fun validateCredentials(email: String, password: String): User? {
//        if (password != MOCK_PASSWORD) return null
//        return getByEmail(email)
//    }
//}