package com.example.proyectofinaldisenomovil.model

data class LikeEvento(
    val id: String,
    val usuarioId: String,
    val eventoId: String,
    val fecha : Long
)