package com.example.proyectofinaldisenomovil.data.BdProvisional

import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.domain.model.Event.EventStatus

/**
 * Datos quemados de eventos comunitarios para desarrollo (Fase 2).
 * Basados en la ciudad de Armenia, Quindío, Colombia.
 * Reemplazar por llamadas reales a Firestore en Fase 3.
 */
object MockEventData {

    val events = listOf(

        // ─── DEPORTES ────────────────────────────────────────────────────────────
        Event(
            id = "evt_001",
            authorUid = "user_001",
            authorName = "Camilo Torres",
            title = "Ciclopaseo nocturno por Armenia",
            description = "Únete al ciclopaseo mensual que recorre los principales parques y avenidas de Armenia. Apto para todas las edades. Trae tu bici, casco y muchas ganas de rodar.",
            category = EventCategory.DEPORTES,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800",
                "https://images.unsplash.com/photo-1541625602330-2277a4c46182?w=800"
            ),
            latitude = 4.5339,
            longitude = -75.6811,
            address = "Parque de la Vida, Armenia, Quindío",
            maxAttendees = 100,
            currentAttendees = 67,
            status = EventStatus.VERIFIED,
            importantVotes = 42,
            isResolved = false,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        ),

        Event(
            id = "evt_002",
            authorUid = "user_002",
            authorName = "Laura Gómez",
            title = "Torneo relámpago de microfútbol",
            description = "Torneo de microfútbol masculino y femenino en la cancha sintética del barrio Centenario. Inscripciones abiertas para equipos de 5 jugadores. Premio para el campeón.",
            category = EventCategory.DEPORTES,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1431324155629-1a6deb1dec8d?w=800"
            ),
            latitude = 4.5412,
            longitude = -75.6743,
            address = "Cancha Sintética Centenario, Armenia, Quindío",
            maxAttendees = 80,
            currentAttendees = 80,
            status = EventStatus.VERIFIED,
            importantVotes = 31,
            isResolved = false,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        ),

        Event(
            id = "evt_003",
            authorUid = "user_003",
            authorName = "Sebastián Ríos",
            title = "Caminata ecológica Parque Miracielos",
            description = "Recorrido guiado de 8 km por senderos naturales del Parque Nacional del Café. Nivel de dificultad: moderado. Incluye avistamiento de aves y parada en mirador.",
            category = EventCategory.DEPORTES,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1551632811-561732d1e306?w=800",
                "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=800"
            ),
            latitude = 4.4885,
            longitude = -75.7123,
            address = "Parque Nacional del Café, Montenegro, Quindío",
            maxAttendees = 30,
            currentAttendees = 22,
            status = EventStatus.VERIFIED,
            importantVotes = 19,
            isResolved = false,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        ),

        // ─── CULTURA ─────────────────────────────────────────────────────────────
        Event(
            id = "evt_004",
            authorUid = "user_004",
            authorName = "Valentina Ospina",
            title = "Concierto de jazz en el Pasaje Uribe",
            description = "Noche de jazz en vivo con tres agrupaciones locales. Entrada libre. Trae tu silla o disfruta de pie. Habrá food trucks y artesanías. Apto para toda la familia.",
            category = EventCategory.CULTURA,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=800",
                "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=800"
            ),
            latitude = 4.5367,
            longitude = -75.6792,
            address = "Pasaje Uribe, Centro, Armenia, Quindío",
            maxAttendees = null,
            currentAttendees = 134,
            status = EventStatus.VERIFIED,
            importantVotes = 88,
            isResolved = false,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        ),

        Event(
            id = "evt_005",
            authorUid = "user_005",
            authorName = "Daniela Salazar",
            title = "Exposición de fotografía: Quindío en colores",
            description = "Muestra fotográfica que celebra la diversidad natural y cultural del departamento. 40 fotografías de artistas locales impresas en gran formato. Entrada gratuita.",
            category = EventCategory.CULTURA,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=800"
            ),
            latitude = 4.5349,
            longitude = -75.6815,
            address = "Museo del Oro Quimbaya, Armenia, Quindío",
            maxAttendees = null,
            currentAttendees = 56,
            status = EventStatus.VERIFIED,
            importantVotes = 27,
            isResolved = false,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        ),

        Event(
            id = "evt_006",
            authorUid = "user_006",
            authorName = "Andrés Castaño",
            title = "Obra de teatro: Crónicas del Eje Cafetero",
            description = "Obra de teatro comunitaria que narra historias de vida de habitantes del Eje Cafetero. Escrita y actuada por jóvenes del barrio La Castellana. Dos funciones: 4pm y 7pm.",
            category = EventCategory.CULTURA,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1503095396549-807759245b35?w=800"
            ),
            latitude = 4.5401,
            longitude = -75.6798,
            address = "Teatro Centenario, Armenia, Quindío",
            maxAttendees = 200,
            currentAttendees = 143,
            status = EventStatus.VERIFIED,
            importantVotes = 54,
            isResolved = false,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        ),

        // ─── ACADÉMICO ───────────────────────────────────────────────────────────
        Event(
            id = "evt_007",
            authorUid = "user_007",
            authorName = "Felipe Cardona",
            title = "Hackathon: Soluciones digitales para el campo",
            description = "48 horas para desarrollar soluciones tecnológicas que mejoren la productividad del agro quindiano. Equipos de 3 a 5 personas. Premios en efectivo y mentoría para los ganadores.",
            category = EventCategory.OTRO,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1504384308090-c894fdcc538d?w=800",
                "https://images.unsplash.com/photo-1517245386807-bb43f82c33c4?w=800"
            ),
            latitude = 4.5278,
            longitude = -75.6834,
            address = "Universidad del Quindío, Armenia",
            maxAttendees = 150,
            currentAttendees = 98,
            status = EventStatus.VERIFIED,
            importantVotes = 73,
            isResolved = false,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        ),

        Event(
            id = "evt_008",
            authorUid = "user_008",
            authorName = "Mariana Herrera",
            title = "Taller gratuito de Inteligencia Artificial para docentes",
            description = "Taller práctico de 6 horas para docentes de primaria y bachillerato. Aprende a integrar herramientas de IA en el aula. Cupos limitados, requiere inscripción previa.",
            category = EventCategory.SOCIAL,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?w=800"
            ),
            latitude = 4.5312,
            longitude = -75.6801,
            address = "SENA Regional Quindío, Armenia",
            maxAttendees = 40,
            currentAttendees = 38,
            status = EventStatus.VERIFIED,
            importantVotes = 45,
            isResolved = false,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        ),

        Event(
            id = "evt_009",
            authorUid = "user_009",
            authorName = "Juliana Mejía",
            title = "Charla: Emprendimiento rural en el Quindío",
            description = "Panel con 4 emprendedores del sector cafetero y turístico del departamento. Se abordarán temas de financiación, mercadeo digital y asociatividad. Entrada libre.",
            category = EventCategory.SOCIAL,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1475721027785-f74eccf877e2?w=800"
            ),
            latitude = 4.5356,
            longitude = -75.6776,
            address = "Cámara de Comercio de Armenia, Quindío",
            maxAttendees = 80,
            currentAttendees = 51,
            status = EventStatus.PENDING_REVIEW,
            importantVotes = 12,
            isResolved = false,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        ),

        // ─── VOLUNTARIADO ─────────────────────────────────────────────────────────
        Event(
            id = "evt_010",
            authorUid = "user_010",
            authorName = "Ricardo Montoya",
            title = "Jornada de limpieza Río Quindío",
            description = "Nos unimos para limpiar las orillas del Río Quindío en el sector de La Isabela. Se proveen bolsas, guantes y refrigerio. Trae ropa que pueda ensuciarse.",
            category = EventCategory.VOLUNTARIADO,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1532996122724-e3c354a0b15b?w=800",
                "https://images.unsplash.com/photo-1558618047-3c8c76ca7d13?w=800"
            ),
            latitude = 4.5198,
            longitude = -75.6901,
            address = "Sector La Isabela, Río Quindío, Armenia",
            maxAttendees = 60,
            currentAttendees = 34,
            status = EventStatus.VERIFIED,
            importantVotes = 61,
            isResolved = false,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        ),

        Event(
            id = "evt_011",
            authorUid = "user_011",
            authorName = "Paola Ángel",
            title = "Colecta de útiles escolares para veredas",
            description = "Recolección de cuadernos, lápices, colores y maletines para niños de zonas rurales del Quindío. Punto de acopio disponible de lunes a sábado de 8am a 6pm.",
            category = EventCategory.VOLUNTARIADO,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1497486751825-1233686d5d80?w=800"
            ),
            latitude = 4.5367,
            longitude = -75.6755,
            address = "Iglesia San Francisco, Armenia, Quindío",
            maxAttendees = null,
            currentAttendees = 89,
            status = EventStatus.VERIFIED,
            importantVotes = 94,
            isResolved = false,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        ),

        // ─── SOCIAL ───────────────────────────────────────────────────────────────
        Event(
            id = "evt_012",
            authorUid = "user_012",
            authorName = "Natalia Duque",
            title = "Feria de emprendedores locales",
            description = "Más de 50 emprendedores del Quindío exhibirán productos artesanales, gastronómicos y de moda. Habrá shows musicales, zona de comidas y actividades para niños. Entrada libre.",
            category = EventCategory.SOCIAL,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1555529669-e69e7aa0ba9a?w=800",
                "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=800"
            ),
            latitude = 4.5378,
            longitude = -75.6820,
            address = "Plaza de Bolívar, Armenia, Quindío",
            maxAttendees = null,
            currentAttendees = 312,
            status = EventStatus.VERIFIED,
            importantVotes = 156,
            isResolved = false,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        ),

        Event(
            id = "evt_013",
            authorUid = "user_013",
            authorName = "David Quintero",
            title = "Bazar navideño barrio El Bosque",
            description = "Bazar comunitario con venta de comidas típicas, artesanías navideñas, ropa y más. Las ganancias apoyan el alumbrado navideño del barrio. ¡Ven en familia!",
            category = EventCategory.SOCIAL,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1513885535751-8b9238bd345a?w=800"
            ),
            latitude = 4.5445,
            longitude = -75.6703,
            address = "Parque El Bosque, Armenia, Quindío",
            maxAttendees = null,
            currentAttendees = 175,
            status = EventStatus.VERIFIED,
            importantVotes = 67,
            isResolved = false,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        ),

        Event(
            id = "evt_014",
            authorUid = "user_014",
            authorName = "Sara Vargas",
            title = "Encuentro de colectivos juveniles del Quindío",
            description = "Espacio de diálogo y conexión entre colectivos culturales, ambientales y sociales del departamento. Networking, presentaciones y cocreación de proyectos comunes.",
            category = EventCategory.SOCIAL,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1529156069898-49953e39b3ac?w=800"
            ),
            latitude = 4.5290,
            longitude = -75.6840,
            address = "Centro Cultural Comfenalco, Armenia, Quindío",
            maxAttendees = 120,
            currentAttendees = 77,
            status = EventStatus.PENDING_REVIEW,
            importantVotes = 23,
            isResolved = false,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        ),

        // ─── EVENTO RESUELTO (para estadísticas) ────────────────────────────────
        Event(
            id = "evt_015",
            authorUid = "user_001",
            authorName = "Camilo Torres",
            title = "Maratón 5K por la paz",
            description = "Carrera pedestre de 5 kilómetros por las principales vías de Armenia. Evento ya realizado con gran éxito y participación ciudadana.",
            category = EventCategory.DEPORTES,
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1452626038306-9aae5e071dd3?w=800"
            ),
            latitude = 4.5339,
            longitude = -75.6811,
            address = "Avenida Bolívar, Armenia, Quindío",
            maxAttendees = 500,
            currentAttendees = 487,
            status = EventStatus.VERIFIED,
            importantVotes = 203,
            isResolved = true,
            startDate = null,
            endDate = null,
            createdAt = null,
            updatedAt = null
        )
    )

    // ─── Helpers de filtrado listos para usar en el ViewModel ────────────────────

    fun getByCategory(category: EventCategory) =
        events.filter { it.category == category }

    fun getVerified() =
        events.filter { it.status == EventStatus.VERIFIED && !it.isResolved }

    fun getPending() =
        events.filter { it.status == EventStatus.PENDING_REVIEW }

    fun getResolved() =
        events.filter { it.isResolved }

    fun getByAuthor(authorUid: String) =
        events.filter { it.authorUid == authorUid }

    fun getById(id: String) =
        events.firstOrNull { it.id == id }
}