package com.example.proyectofinaldisenomovil.data.repository.Memory

import com.example.proyectofinaldisenomovil.data.repository.AttendanceRepository
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.Attendance
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceRepositoryImpl @Inject constructor(): AttendanceRepository {
    
    // Almacenamos las asistencias: eventId -> Set de UserIds
    private val attendanceMap = mutableMapOf<String, MutableSet<String>>()
    
    // Para el observeUserAttendances necesitamos algo más dinámico
    private val _allAttendances = MutableStateFlow<List<Attendance>>(emptyList())

    override suspend fun confirmAttendance(eventId: String, uid: String) {
        val attendees = attendanceMap.getOrPut(eventId) { mutableSetOf() }
        if (attendees.add(uid)) {
            // Incrementar contador en el MockDataRepository
            val event = MockDataRepository.getEventById(eventId)
            event?.let {
                MockDataRepository.updateEvent(it.copy(currentAttendees = it.currentAttendees + 1))
            }
            
            // Actualizar flujo de asistencias
            val newAttendance = Attendance(
                uid = uid,
                eventId = eventId,
                confirmedAt = Timestamp.now()
            )
            _allAttendances.value = _allAttendances.value + newAttendance
        }
    }

    override suspend fun cancelAttendance(eventId: String, uid: String) {
        val attendees = attendanceMap[eventId]
        if (attendees?.remove(uid) == true) {
            // Decrementar contador en el MockDataRepository
            val event = MockDataRepository.getEventById(eventId)
            event?.let {
                MockDataRepository.updateEvent(it.copy(currentAttendees = (it.currentAttendees - 1).coerceAtLeast(0)))
            }
            
            // Actualizar flujo de asistencias
            _allAttendances.value = _allAttendances.value.filterNot { it.eventId == eventId && it.uid == uid }
        }
    }

    override suspend fun isAttending(eventId: String, uid: String): Boolean {
        return attendanceMap[eventId]?.contains(uid) ?: false
    }

    override suspend fun getAttendeeCount(eventId: String): Int {
        return attendanceMap[eventId]?.size ?: 0
    }

    override fun observeUserAttendances(uid: String): Flow<List<Attendance>> {
        return _allAttendances.map { list -> list.filter { it.uid == uid } }
    }
}
