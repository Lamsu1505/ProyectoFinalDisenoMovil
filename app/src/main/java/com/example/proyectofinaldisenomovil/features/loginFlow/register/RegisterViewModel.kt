package com.example.proyectofinaldisenomovil.features.login

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {


    var name by mutableStateOf("")
    var passwordConfirmation by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by  mutableStateOf("")
    var password by  mutableStateOf("")
    var emailError by  mutableStateOf("")
    var passwordError by  mutableStateOf("")
    var passwordConfirmationError by  mutableStateOf("")


    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    fun onEmailChange(newEmail: String) {
        this.email = newEmail
        validateEmail(email)
    }

    fun onPasswordChange(newPassword: String) {
        this.password = newPassword
        validatePassword(password)
    }

    fun validateEmail(email: String) {
        emailError =
            if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                "Email mal escrito"
            } else {
                ""
            }
    }

    fun validatePassword(password : String) {
        if (password.length > 0 && password.length < 8 ) {
            passwordError = "La contraseña debe tener al menos 8 caracteres"
        } else {
            passwordError = ""
        }
    }

    fun validateForm() : Boolean{
        if(emailError.isEmpty() && passwordError.isEmpty() &&
            email.isNotEmpty() && password.isNotEmpty()
            && passwordConfirmationError.isEmpty() && passwordConfirmation.isNotEmpty()
            && name.isNotEmpty() && lastName.isNotEmpty()){
            return true
        }
        return false
    }

    fun onNameChange(newName: String) {
        this.name = newName
    }

    fun onLastNameChange(newLastName: String) {
        this.lastName = newLastName
    }

    fun onPasswordConfirmationChange(newPasswordConfirmation: String) {
        this.passwordConfirmation = newPasswordConfirmation
        validatePasswordConfirmation(passwordConfirmation)
    }

    private fun validatePasswordConfirmation(passwordConfirmation: String) {
        if (passwordConfirmation != password) {
            passwordConfirmationError = "Las contraseñas no coinciden"
        }
        if(passwordConfirmation.isEmpty() || passwordConfirmation == password){
            passwordConfirmationError = ""
        }
    }

    fun registerUser(onResult: (Boolean) -> Unit) {


    }
}