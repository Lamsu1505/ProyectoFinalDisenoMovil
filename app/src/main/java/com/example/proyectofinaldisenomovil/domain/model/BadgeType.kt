package com.example.proyectofinaldisenomovil.domain.model

/**
 * Collectible badges a user can earn through participation.
 *
 * Stored as a [List]<[BadgeType]> inside the [com.example.proyectofinaldisenomovil.domain.model.User.User] document.
 *
 * @property label       Short badge name shown in the UI chip/card.
 * @property description Tooltip / achievement description.
 * @property iconRes     Resource name of the badge drawable (prefix "ic_badge_").
 * @property category    Badge category for UI grouping.
 */
enum class BadgeType(
    val label: String,
    val description: String,
    val iconRes: String,
    val category: BadgeCategory
) {
    // === CREADOR: Crear eventos ===
    PRIMERA_PUBLICACION(
        label       = "Primera Publicación",
        description = "Creaste tu primer evento",
        iconRes     = "ic_badge_first_event",
        category    = BadgeCategory.CREADOR
    ),
    PRODUCTOR(
        label       = "Productor",
        description = "5 eventos creados",
        iconRes     = "ic_badge_producer",
        category    = BadgeCategory.CREADOR
    ),
    ORGANIZADOR_EXPERTO(
        label       = "Organizador Experto",
        description = "10 eventos verificados",
        iconRes     = "ic_badge_expert_organizer",
        category    = BadgeCategory.CREADOR
    ),
    MAESTRO_EVENTOS(
        label       = "Maestro de Eventos",
        description = "25 eventos creados",
        iconRes     = "ic_badge_master",
        category    = BadgeCategory.CREADOR
    ),

    // === SOCIAL: Participar en eventos ===
    PRIMER_PASO(
        label       = "Primer Paso",
        description = "Primera asistencia confirmada",
        iconRes     = "ic_badge_first_step",
        category    = BadgeCategory.SOCIAL
    ),
    ASISTENTE_FIEL(
        label       = "Asistente Fiel",
        description = "10 asistencias confirmadas",
        iconRes     = "ic_badge_faithful",
        category    = BadgeCategory.SOCIAL
    ),
    VETERANO(
        label       = "Veterano",
        description = "25 asistencias confirmadas",
        iconRes     = "ic_badge_veteran",
        category    = BadgeCategory.SOCIAL
    ),
    LEYENDA(
        label       = "Leyenda",
        description = "50 asistencias confirmadas",
        iconRes     = "ic_badge_legend",
        category    = BadgeCategory.SOCIAL
    ),

    // === COMUNIDAD: Comentar ===
    PRIMER_COMENTARIO(
        label       = "Primer Comentario",
        description = "Dejaste tu primer comentario",
        iconRes     = "ic_badge_first_comment",
        category    = BadgeCategory.COMUNIDAD
    ),
    CONVERSADOR(
        label       = "Conversador",
        description = "25 comentarios publicados",
        iconRes     = "ic_badge_talker",
        category    = BadgeCategory.COMUNIDAD
    ),
    HISTORIADOR(
        label       = "Historiador",
        description = "50 comentarios publicados",
        iconRes     = "ic_badge_historian",
        category    = BadgeCategory.COMUNIDAD
    ),
    VOZ_COMUNIDAD(
        label       = "Voz de la Comunidad",
        description = "100 comentarios publicados",
        iconRes     = "ic_badge_voice",
        category    = BadgeCategory.COMUNIDAD
    ),

    // === POPULAR: Votos y reputación ===
    RELEVANTE(
        label       = "Relevante",
        description = "10 votos recibidos en tus eventos",
        iconRes     = "ic_badge_relevant",
        category    = BadgeCategory.POPULAR
    ),
    POPULAR(
        label       = "Popular",
        description = "50 votos recibidos",
        iconRes     = "ic_badge_popular",
        category    = BadgeCategory.POPULAR
    ),
    ESTRELLA(
        label       = "Estrella",
        description = "100 votos recibidos",
        iconRes     = "ic_badge_star",
        category    = BadgeCategory.POPULAR
    ),
    ICONO(
        label       = "Ícono",
        description = "250 votos recibidos",
        iconRes     = "ic_badge_icon",
        category    = BadgeCategory.POPULAR
    ),

    // === ESPECIALES ===
    FUNDADOR(
        label       = "Fundador",
        description = "Usuario de la primera hora",
        iconRes     = "ic_badge_founder",
        category    = BadgeCategory.ESPECIAL
    ),
    EARLY_ADOPTER(
        label       = "Early Adopter",
        description = "Uno de los primeros 100 usuarios",
        iconRes     = "ic_badge_early_adopter",
        category    = BadgeCategory.ESPECIAL
    ),
    MODERATOR(
        label       = "Moderador",
        description = "Equipo de moderación de la plataforma",
        iconRes     = "ic_badge_moderator",
        category    = BadgeCategory.ESPECIAL
    ),
    VOLUNTARIO(
        label       = "Voluntario",
        description = "Participaste en eventos de voluntariado",
        iconRes     = "ic_badge_volunteer",
        category    = BadgeCategory.ESPECIAL
    ),
}

enum class BadgeCategory(val label: String) {
    CREADOR("Creador"),
    SOCIAL("Social"),
    COMUNIDAD("Comunidad"),
    POPULAR("Popular"),
    ESPECIAL("Especiales")
}
