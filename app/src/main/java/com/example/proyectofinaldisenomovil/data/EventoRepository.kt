package com.example.proyectofinaldisenomovil.data

import com.example.proyectofinaldisenomovil.model.Evento

interface EventoRepository {

    suspend fun crearEvento(evento: Evento)

    suspend fun obtenerEventos(): List<Evento>

    suspend fun obtenerEventoPorId(id: String): Evento?

    suspend fun finalizarEvento(id: String)

    suspend fun verificarEvento(id: String, verificado: Boolean, motivo: String?)
}