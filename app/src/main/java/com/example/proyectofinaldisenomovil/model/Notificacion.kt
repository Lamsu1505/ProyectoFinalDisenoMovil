package com.example.proyectofinaldisenomovil.model

data class Notificacion(
    val id: String,
    val usuarioId: String,
    val titulo: String,
    val mensaje: String,
    val leida: Boolean = false,
    val fecha: Long,
    val maxCaracteres : Int = 100
)