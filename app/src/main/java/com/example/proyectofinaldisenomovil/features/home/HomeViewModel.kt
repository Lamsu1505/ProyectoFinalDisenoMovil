package com.example.proyectofinaldisenomovil.features.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {
    private val auth: FirebaseAuth? =
        try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            null
        }
}