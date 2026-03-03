package com.example.proyectofinaldisenomovil.features.register

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.navigation.AppScreens
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.features.login.RegisterViewModel
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.component.login.HeaderSectionNonLogued
import com.example.proyectofinaldisenomovil.core.component.login.TopBarRegister
import kotlin.Result.Companion.success

@Composable
fun RegisterScreen (
    navController: NavController,
    registerViewModel: RegisterViewModel = viewModel()
){
    val myContext = LocalContext.current

    Scaffold(
        topBar = {
            TopBarRegister(navController)
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HeaderSectionNonLogued(navController)

            val cardShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            Card(
                shape = cardShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(5.dp)
                    .offset(y = 130.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp, vertical = 40.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(50.dp)
                ) {
                    RegisterForm(
                        navController,
                        registerViewModel,
                        myContext
                    )
                }
            }
        }
    }
}

@Composable
fun RegisterForm(navController: NavController, registerViewModel: RegisterViewModel, myContext : Context) {

    Text(
        text = "Registrarse",
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )

    //Formulario de registro
    Column(
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // ---------------- Nombre Apellido ----------------
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {

                OutlinedTextField(
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp),
                    value = registerViewModel.name,
                    modifier = Modifier.weight(1f),
                    onValueChange = {
                        registerViewModel.onNameChange(it)
                    },
                    label = { Text("Nombre(s)") },
                )

                OutlinedTextField(
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp),
                    value = registerViewModel.lastName,
                    modifier = Modifier.weight(1f),
                    onValueChange = {
                        registerViewModel.onLastNameChange(it)
                    },
                    label = { Text("Apellido(s)") },
                )
            }


        }

        // ---------------- Correo ----------------

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                value = registerViewModel.email,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    registerViewModel.onEmailChange(it)
                },
                label = { Text("Correo electronico") },
                isError = registerViewModel.emailError.isNotEmpty()
            )

            if (registerViewModel.emailError.isNotEmpty()){
                Box(modifier = Modifier.height(17.dp)) {
                    Text(
                        text = "Email mal escrito, revisa el formato",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        }


        //------------ Contraseñas --------------
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                value = registerViewModel.password,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = {
                    registerViewModel.onPasswordChange(it)
                },
                label = { Text("Contraseña secreta") },
                isError = registerViewModel.passwordError.isNotEmpty()
            )

            Box(modifier = Modifier.height(17.dp)) {
                if (registerViewModel.passwordError.isNotEmpty()) {
                    Text(
                        text = "La contraseña debe tener al menos 8 caracteres",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }


            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                value = registerViewModel.passwordConfirmation,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = {
                    registerViewModel.onPasswordConfirmationChange(it)
                },
                label = { Text("Confirmar contraseña secreta") },
                isError = registerViewModel.passwordError.isNotEmpty()
            )

            Box(modifier = Modifier.height(17.dp)) {
                if (registerViewModel.passwordConfirmationError.isNotEmpty()) {
                    Text(
                        text = "Las contraseñas no coinciden",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Button(
            enabled = registerViewModel.validateForm(),
            onClick = {
                registerViewModel.registerUser { success ->
                    if (success) {
                        navController.navigate(AppScreens.LoginScreen.route)
                    }
                }
            },
            modifier = Modifier.height(50.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                disabledContentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(
                text = "Registrarse",
                fontSize = 20.sp
            )
        }
    }



}


@Preview(showBackground = true)
@Composable
fun RegisterPreview(){
    ProyectoFinalDisenoMovilTheme {
        RegisterScreen(
            navController = rememberNavController())
    }
}