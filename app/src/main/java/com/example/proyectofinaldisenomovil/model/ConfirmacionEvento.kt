package com.example.proyectofinaldisenomovil.model

data class ConfirmacionEvento(
    //Clase que representa la confirmacion de un evento por parte de un usuario
    val id: String,
    val usuarioId: String,
    val eventoId: String,
    val fechaConfirmacion: Long
)