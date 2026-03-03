package com.example.proyectofinaldisenomovil.features.ForgotPassword

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {


    private val auth: FirebaseAuth? =
        try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            null
        }

    var email by mutableStateOf("")
    var emailError by mutableStateOf("")

    fun onEmailChange(newEmail: String) {
        this.email = newEmail
        validateEmail(email)
    }

    fun validateEmail(email: String) {
        emailError =
            if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                "Email mal escrito"
            } else {
                ""
            }
    }

    fun validateForm(): Boolean {
        return false
    }

}