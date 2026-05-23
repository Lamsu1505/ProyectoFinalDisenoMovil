package com.example.proyectofinaldisenomovil.domain.model

import com.example.proyectofinaldisenomovil.R

/**
 * Collectible badges a user can earn through participation.
 *
 * Stored as a [List]<[String]> (storing the enum names) inside the User document.
 *
 * @property label       Short badge name shown in the UI chip/card.
 * @property description Tooltip / achievement description.
 * @property img         Mipmap resource ID of the badge image.
 * @property category    Badge category for UI grouping.
 */
enum class BadgeType(
    val label: String,
    val description: String,
    val img: Int,
    val category: BadgeCategory
) {
    // === PUBLICACIONES VERIFICADAS ===
    PRIMERA_PUBLICACION(
        label       = "Iniciador",
        description = "Primer evento verificado",
        img         = R.mipmap.badget_1event,
        category    = BadgeCategory.CREADOR
    ),
    CINCO_PUBLICACIONES(
        label       = "Productor",
        description = "5 eventos verificados",
        img         = R.mipmap.badget_5events,
        category    = BadgeCategory.CREADOR
    ),
    VEINTE_PUBLICACIONES(
        label       = "Organizador Experto",
        description = "20 eventos verificados",
        img         = R.mipmap.badget_20events,
        category    = BadgeCategory.CREADOR
    ),

    // === LIKES RECIBIDOS ===
    CINCUENTA_LIKES(
        label       = "Relevante",
        description = "50+ likes en tus publicaciones",
        img         = R.mipmap.badget_50likes,
        category    = BadgeCategory.POPULAR
    ),
    DOSCIENTOS_LIKES(
        label       = "Popular",
        description = "200+ likes en tus publicaciones",
        img         = R.mipmap.badget_200likes,
        category    = BadgeCategory.POPULAR
    ),
    MIL_LIKES(
        label       = "Ícono",
        description = "1000+ likes en tus publicaciones",
        img         = R.mipmap.badget_1000likes,
        category    = BadgeCategory.POPULAR
    ),

    // === ANTIGUEDAD ===
    UN_ANO_ANTIGUEDAD(
        label       = "Veterano",
        description = "1+ año de antigüedad",
        img         = R.mipmap.badget_1year,
        category    = BadgeCategory.ESPECIAL
    ),
}

enum class BadgeCategory(val label: String) {
    CREADOR("Creador"),
    POPULAR("Popular"),
    ESPECIAL("Especiales")
}
