package com.example.proyectofinaldisenomovil.core.utils

import android.util.Patterns

data class ValidatedField(
    val value: String = "",
    val error: String? = null,
    val isValid: Boolean = false
)

object Validators {
    fun validateEmail(email: String): String? {
        return when {
            email.isEmpty() -> null
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "validation_email_invalid"
            else -> null
        }
    }

    fun validatePassword(password: String, minLength: Int = 8): String? {
        return when {
            password.isEmpty() -> null
            password.length < minLength -> "validation_password_short"
            else -> null
        }
    }

    fun validateRequired(value: String, fieldName: String = "field"): String? {
        return when {
            value.isEmpty() -> "$fieldName required"
            else -> null
        }
    }
}
