package com.example.proyectofinaldisenomovil.data.repository.Remote

import android.util.Log
import com.example.proyectofinaldisenomovil.data.repository.AttendanceRepository
import com.example.proyectofinaldisenomovil.domain.model.Attendance
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AttendanceRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): AttendanceRepository {


    override suspend fun confirmAttendance(
        eventId: String,
        uid: String
    ) {
        try {
            val attendanceId = "${eventId}_${uid}"
            val attendanceRef = firestore
                .collection("attendances")
                .document(attendanceId)

            // Verificar si ya existe
            val document = attendanceRef
                .get()
                .await()

            if (!document.exists()) {
                val attendance = Attendance(
                    id = attendanceId,
                    eventId = eventId,
                    uid = uid,
                    confirmedAt = Timestamp.now()
                )

                attendanceRef
                    .set(attendance)
                    .await()

                // Incrementar contador del evento
                firestore
                    .collection("events")
                    .document(eventId)
                    .update(
                        "currentAttendees",
                        FieldValue.increment(1)
                    )
                    .await()
            }
        } catch (e: Exception) {
            Log.e(
                "CONFIRM_ATTENDANCE",
                e.stackTraceToString()
            )
        }
    }

    override suspend fun cancelAttendance(
        eventId: String,
        uid: String
    ) {
        try {
            val attendanceId = "${eventId}_${uid}"

            val attendanceRef = firestore
                .collection("attendances")
                .document(attendanceId)

            val document = attendanceRef
                .get()
                .await()

            if (document.exists()) {
                attendanceRef
                    .delete()
                    .await()

                firestore
                    .collection("events")
                    .document(eventId)
                    .update(
                        "currentAttendees",
                        FieldValue.increment(-1)
                    )
                    .await()
            }

        } catch (e: Exception) {

            Log.e(
                "CANCEL_ATTENDANCE",
                e.stackTraceToString()
            )
        }
    }

    override suspend fun isAttending(
        eventId: String,
        uid: String
    ): Boolean {

        return try {
            val attendanceId = "${eventId}_${uid}"

            firestore
                .collection("attendances")
                .document(attendanceId)
                .get()
                .await()
                .exists()

        } catch (e: Exception) {

            false
        }
    }

    override suspend fun getAttendeeCount(
        eventId: String
    ): Int {
        return try {
            val snapshot = firestore
                .collection("attendances")
                .whereEqualTo("eventId", eventId)
                .get()
                .await()

            snapshot.size()

        } catch (e: Exception) {
            0
        }
    }

    override fun observeUserAttendances(
        uid: String
    ): Flow<List<Attendance>> = callbackFlow {

        val listener = firestore
            .collection("attendances")
            .whereEqualTo("uid", uid)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val attendances = snapshot?.documents
                    ?.mapNotNull {
                        it.toObject(Attendance::class.java)
                    }
                    ?: emptyList()

                trySend(attendances)
            }

        awaitClose {
            listener.remove()
        }
    }

    override suspend fun getEventsIdByUserID(
        uid: String
    ): List<String> {
        return try {
            firestore
                .collection("attendances")
                .whereEqualTo("uid", uid)
                .get()
                .await()
                .documents
                .mapNotNull {
                    it.getString("eventId")
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun fetchAttendances(): List<Attendance> {
        TODO("Not yet implemented")
    }
}