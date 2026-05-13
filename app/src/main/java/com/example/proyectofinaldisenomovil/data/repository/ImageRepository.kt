package com.example.proyectofinaldisenomovil.data.repository

import android.net.Uri

interface ImageRepository {
    /**
     * Sube una imagen a Cloudinary y retorna la URL
     */
    suspend fun uploadImage(uri: Uri): String?
}
