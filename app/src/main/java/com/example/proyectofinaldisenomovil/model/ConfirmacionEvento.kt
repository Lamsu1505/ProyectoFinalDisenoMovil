package com.example.proyectofinaldisenomovil.model

data class ConfirmacionEvento(
    val id: String,
    val usuarioId: String,
    val eventoId: String,
    val fechaConfirmacion: Long
)