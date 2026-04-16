//package com.example.proyectofinaldisenomovil.data.BdProvisional
//
//import com.example.proyectofinaldisenomovil.domain.model.AppNotification
//import com.example.proyectofinaldisenomovil.domain.model.NotificationType
//import java.util.Date
//
///**
// * Datos quemados de notificaciones para desarrollo.
// */
//object MockNotificationData {
//
//    val notifications = listOf(
//        AppNotification(
//            id = "notif_001",
//            recipientUid = "user_001",a
//            type = NotificationType.VERIFIED,
//            title = "Evento aprobado",
//            body = "Tu evento 'Ciclopaseo nocturno por Armenia' ha sido aprobado y está publicado.",
//            eventId = "evt_001",
//            isRead = false,
//            createdAt = com.google.firebase.Timestamp.now()
//        ),
//        AppNotification(
//            id = "notif_002",
//            recipientUid = "user_001",
//            type = NotificationType.COMMENT,
//            title = "Nuevo comentario",
//            body = "Laura Gómez comentou en tu evento 'Ciclopaseo nocturno por Armenia'",
//            eventId = "evt_001",
//            isRead = true,
//            createdAt = com.google.firebase.Timestamp(Date(System.currentTimeMillis() - 86400000))
//        ),
//        AppNotification(
//            id = "notif_003",
//            recipientUid = "user_001",
//            type = NotificationType.NEW_EVENT,
//            title = "Nuevo asistente",
//            body = "Sebastián Ríos confirmó asistencia a tu evento",
//            eventId = "evt_001",
//            isRead = false,
//            createdAt = com.google.firebase.Timestamp(Date(System.currentTimeMillis() - 172800000))
//        ),
//        AppNotification(
//            id = "notif_004",
//            recipientUid = "user_001",
//            type = NotificationType.NEW_EVENT_NEARBY,
//            title = "Nuevo evento cerca",
//            body = "Hay un nuevo evento cerca de ti: 'Concierto de jazz en el Pasaje Uribe'",
//            eventId = "evt_004",
//            isRead = false,
//            createdAt = com.google.firebase.Timestamp(Date(System.currentTimeMillis() - 259200000))
//        ),
//        AppNotification(
//            id = "notif_005",
//            recipientUid = "user_002",
//            type = NotificationType.REJECTED,
//            title = "Evento rechazado",
//            body = "Tu evento 'Charla de tecnología' fue rechazado. Revisa el motivo.",
//            eventId = "evt_009",
//            isRead = false,
//            createdAt = com.google.firebase.Timestamp.now()
//        )
//    )
//
//    fun getByUserId(uid: String) =
//        notifications.filter { it.recipientUid == uid }
//            .sortedByDescending { it.createdAt }
//
//    fun getUnreadCount(uid: String) =
//        notifications.count { it.recipientUid == uid && !it.isRead }
//}