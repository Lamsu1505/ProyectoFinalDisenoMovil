package com.example.proyectofinaldisenomovil.model

import com.example.proyectofinaldisenomovil.model.enums.NivelUsuario
import com.example.proyectofinaldisenomovil.model.enums.Rol

data class Usuario(
    val id: String,
    val nombre: String,
    val apellido : String,
    val email: String,
    val puntos: Int = 0,
    val nivel: NivelUsuario = NivelUsuario.ESPECTADOR,
    val rol: Rol = Rol.USUARIO,
    val fotoPerflURL: String? = null,
    var insignias: MutableList<Insignia> = mutableListOf()
)