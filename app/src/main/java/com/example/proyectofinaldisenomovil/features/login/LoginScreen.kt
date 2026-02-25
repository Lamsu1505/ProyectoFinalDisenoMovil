package com.example.proyectofinaldisenomovil.features.login

import android.graphics.drawable.Icon
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectofinaldisenomovil.core.navigation.AppScreens

@Composable
fun LoginFunction(
    navController : NavController ,
    loginViewModel : LoginViewModel = viewModel()
    ) {

    val Mycontext = LocalContext.current

    Log.i("LoginScreen" , "Hay Recomposicion")

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy( 16.dp , Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = loginViewModel.email,
            onValueChange = {
                if(!Patterns.EMAIL_ADDRESS.matcher(it).matches()){
                    loginViewModel.emailError = "Error"
                }
                else{
                    loginViewModel.emailError = ""
                }
                loginViewModel.email = it
            },
            label = { Text("Email") },
            isError = loginViewModel.emailError.isNotEmpty(),
            supportingText = {
                if(loginViewModel.emailError.isNotEmpty()){
                    Text ("Email mal escrito")
                }
            }

        )

        OutlinedTextField(
            value = loginViewModel.password,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                loginViewModel.password = it
             },
            label = { Text("Contraseña") },
        )

        Button(
            onClick = {
                if(loginViewModel.login()){
                    Toast.makeText(
                        Mycontext,
                        "Login Correcto",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate(AppScreens.FirstScreen.route)
                }

            }
        ) {
            Text("Iniciar Sesión")
        }
    }


}