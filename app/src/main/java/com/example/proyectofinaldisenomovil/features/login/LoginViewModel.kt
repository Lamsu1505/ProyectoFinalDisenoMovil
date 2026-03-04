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

class LoginViewModel : ViewModel() {

    var email by  mutableStateOf("")
    var password by  mutableStateOf("")
    var emailError by  mutableStateOf("")

    var passwordError by  mutableStateOf("")

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
        if(emailError.isEmpty() && passwordError.isEmpty() && email.isNotEmpty() && password.isNotEmpty()){
            return true
        }
        return false
    }


    fun login(onResult: (Boolean) -> Unit) {
    }

}