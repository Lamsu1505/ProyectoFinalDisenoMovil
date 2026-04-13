package com.example.proyectofinaldisenomovil.features.loginFlow.register

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.User.UserRole

sealed class RegisterResult {
    data object Idle : RegisterResult()
    data object Loading : RegisterResult()
    data object Success : RegisterResult()
    data object EmailAlreadyExists : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}

class RegisterViewModel : ViewModel() {

    var name by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordConfirmation by mutableStateOf("")
    
    var nameError by mutableStateOf(false)
    var lastNameError by mutableStateOf(false)
    var emailError by mutableStateOf(false)
    var passwordError by mutableStateOf(false)
    var passwordErrorShort by mutableStateOf(false)
    var passwordErrorUppercase by mutableStateOf(false)
    var passwordErrorDigit by mutableStateOf(false)
    var passwordConfirmationError by mutableStateOf(false)

    private val _registerResult = mutableStateOf<RegisterResult>(RegisterResult.Idle)
    val registerResult: RegisterResult get() = _registerResult.value

    fun onNameChange(newName: String) {
        name = newName
        nameError = if (name.isNotEmpty() && name.length < 2) true else false
    }

    fun onLastNameChange(newLastName: String) {
        lastName = newLastName
        lastNameError = if (lastName.isNotEmpty() && lastName.length < 2) true else false
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
        validateEmail(email)
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        validatePassword(password)
        if (passwordConfirmation.isNotEmpty()) {
            validatePasswordConfirmation(passwordConfirmation)
        }
    }

    fun onPasswordConfirmationChange(newConfirmation: String) {
        passwordConfirmation = newConfirmation
        validatePasswordConfirmation(passwordConfirmation)
    }

    private fun validateEmail(email: String) {
        emailError = if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) true else false
    }

    private fun validatePassword(password: String) {
        passwordErrorShort = if (password.isNotEmpty() && password.length < 8) true else false
        passwordErrorUppercase = if (password.isNotEmpty() && !password.any { it.isUpperCase() }) true else false
        passwordErrorDigit = if (password.isNotEmpty() && !password.any { it.isDigit() }) true else false
        passwordError = passwordErrorShort || passwordErrorUppercase || passwordErrorDigit
    }

    private fun validatePasswordConfirmation(confirmation: String) {
        passwordConfirmationError = if (confirmation.isNotEmpty() && confirmation != password) true else false
    }

    fun validateForm(): Boolean {
        return !nameError && !lastNameError && !emailError && !passwordError && !passwordConfirmationError &&
               name.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && 
               password.isNotEmpty() && passwordConfirmation.isNotEmpty()
    }

    fun register() {
        if (!validateForm()) {
            _registerResult.value = RegisterResult.Error("register_complete_fields")
            return
        }

        _registerResult.value = RegisterResult.Loading

        val user = MockDataRepository.registerUser(
            firstName = name.trim(),
            lastName = lastName.trim(),
            email = email.trim().lowercase(),
            password = password
        )

        _registerResult.value = when {
            user == null -> RegisterResult.EmailAlreadyExists
            else -> RegisterResult.Success
        }
    }

    fun resetResult() {
        _registerResult.value = RegisterResult.Idle
    }

    fun clearForm() {
        name = ""
        lastName = ""
        email = ""
        password = ""
        passwordConfirmation = ""
        nameError = false
        lastNameError = false
        emailError = false
        passwordErrorShort = false
        passwordErrorUppercase = false
        passwordErrorDigit = false
        passwordError = false
        passwordConfirmationError = false
        _registerResult.value = RegisterResult.Idle
    }
}
