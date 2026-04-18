package com.example.proyectofinaldisenomovil.data.repository.Memory

import com.example.proyectofinaldisenomovil.data.repository.AttendanceRepository
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.Attendance
import com.example.proyectofinaldisenomovil.domain.model.User.User
import com.example.proyectofinaldisenomovil.domain.model.Vote
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map
import kotlin.collections.mutableSetOf
import kotlin.collections.set

@Singleton
class AttendanceRepositoryImpl @Inject constructor(): AttendanceRepository {

    // Para el observeUserAttendances necesitamos algo más dinámico
    private val _allAttendances = MutableStateFlow<List<Attendance>>(emptyList())

    init{
        _allAttendances.value = fetchAttendances()
    }

    override fun fetchAttendances() : List<Attendance> {
        return listOf(
            Attendance(
                uid = "user_001",
                eventId = "event_001",
                confirmedAt = Timestamp.now()
            ),
            Attendance(
                uid = "user_001",
                eventId = "event_013",
                confirmedAt = Timestamp.now()
            )
        )

    }

    override suspend fun confirmAttendance(eventId: String, uid: String) {

        val current = _allAttendances.value.toMutableList()

        val attendanceId = "${eventId}_${uid}"

        val alreadyExists = current.any{it.id == attendanceId}
        if (!alreadyExists) {
            current.add(
                Attendance(
                    id = attendanceId,
                    eventId = eventId,
                    uid = uid,
                    confirmedAt = Timestamp.now()
                )
            )
            _allAttendances.value = current

            val event = MockDataRepository.getEventById(eventId)
            event?.let {
                MockDataRepository.updateEvent(
                    it.copy(currentAttendees = it.currentAttendees + 1)
                )
            }
            }
        }

    override suspend fun cancelAttendance(eventId: String, uid: String) {
        val current = _allAttendances.value

        val updated = current.filterNot {
            it.eventId == eventId && it.uid == uid
        }

        if(updated.size != current.size){
            _allAttendances.value = updated

            val event = MockDataRepository.getEventById(eventId)
            event?.let {
                MockDataRepository.updateEvent(
                    it.copy(currentAttendees = (it.currentAttendees - 1).coerceAtLeast(0))
                )
            }
        }
    }

    override suspend fun isAttending(eventId: String, uid: String): Boolean {
        return _allAttendances.value.any {
            it.eventId == eventId && it.uid == uid
        }
    }

    override suspend fun getAttendeeCount(eventId: String): Int {
        return _allAttendances.value.count { it.eventId == eventId }
    }

    override fun observeUserAttendances(uid: String): Flow<List<Attendance>> {
        return _allAttendances.map { list -> list.filter { it.uid == uid } }
    }

    override fun getEventsIdByUserID(uid: String): List<String> {
        // 1. Filtramos los votos del StateFlow para obtener solo los del usuario solicitado
        val userSaved = _allAttendances.value.filter { it.uid == uid }
        // 2. Extraemos los IDs de los eventos de los votos del usuario
        val eventIds = userSaved.map { it.eventId }
        // 3. Devolvemos la lista de IDs de eventos
        return eventIds
    }
}
