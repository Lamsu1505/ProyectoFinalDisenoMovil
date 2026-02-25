package com.example.proyectofinaldisenomovil.features.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var email by  mutableStateOf("")
    var password by  mutableStateOf("")
    var emailError by  mutableStateOf("")

    fun onEmailChange(newEmail: String) {
        validateEmail(email)
        this.email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        this.password = newPassword
    }

    fun validateEmail(email : String) {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailError = "Email incorrecto"
        }
        else{
            emailError = ""

        }
    }

    fun validatePassword(): Boolean{
        //TODO
        return false
    }


    fun login(): Boolean{
        return email=="perezmartinezandres0@gmail.com" && password=="123"
    }




}