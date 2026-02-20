package com.example.proyectofinaldisenomovil.model

import com.example.proyectofinaldisenomovil.model.enums.CategoriaEvento
import com.example.proyectofinaldisenomovil.model.enums.EstadoEvento

data class Evento(
    val id: String,
    val titulo: String,
    val descripcion: String?,
    val categoria: CategoriaEvento,
    val creadorId: String,
    val ubicacion: Ubicacion,
    val imagenUrl: String,
    val fechaInicio: Long,
    val fechaFin: Long,
    val cupoMaximo: Int?,
//    var asistentesConfirmados: Int = 0,
    val motivoRechazo: String?,
    var verificado: Boolean = false,
    var estado: EstadoEvento = EstadoEvento.ACTIVO,
//    var votosImportante: Int = 0



)
