package com.example.proyectofinaldisenomovil.model

data class Comentario(
    val id: String,
    val eventoId: String,
    val autorId: String,
    val contenido: String,
    val fecha: Long,
    val maxCaracteres: Int = 200
)
