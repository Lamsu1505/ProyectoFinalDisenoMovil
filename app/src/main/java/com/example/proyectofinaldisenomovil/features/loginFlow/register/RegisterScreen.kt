package com.example.proyectofinaldisenomovil.features.loginFlow.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinaldisenomovil.core.component.login.HeaderSectionNonLogued
import com.example.proyectofinaldisenomovil.core.component.login.TopBarRegister
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(registerViewModel.registerResult) {
        when (registerViewModel.registerResult) {
            is RegisterResult.Success -> {
                snackbarHostState.showSnackbar("Registro exitoso. Ahora puedes iniciar sesión.")
                registerViewModel.clearForm()
                onNavigateToLogin()
            }
            is RegisterResult.EmailAlreadyExists -> {
                snackbarHostState.showSnackbar("El email ya está registrado")
                registerViewModel.resetResult()
            }
            is RegisterResult.Error -> {
                snackbarHostState.showSnackbar((registerViewModel.registerResult as RegisterResult.Error).message)
                registerViewModel.resetResult()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopBarRegister(onBackClick)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HeaderSectionNonLogued()

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
                        registerViewModel,
                        onNavigateToLogin
                    )
                }
            }
        }
    }
}

@Composable
fun RegisterForm(
    registerViewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit
) {

    var passwordVisible by remember { mutableStateOf(false) }
    var repeatPasswordVisible by remember { mutableStateOf(false) }

    Text(
        text = "Registrarse",
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                value = registerViewModel.name,
                modifier = Modifier.weight(1f),
                onValueChange = { registerViewModel.onNameChange(it) },
                label = { Text("Nombre(s)") },
                isError = registerViewModel.nameError.isNotEmpty()
            )

            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                value = registerViewModel.lastName,
                modifier = Modifier.weight(1f),
                onValueChange = { registerViewModel.onLastNameChange(it) },
                label = { Text("Apellido(s)") },
                isError = registerViewModel.lastNameError.isNotEmpty()
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                value = registerViewModel.email,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { registerViewModel.onEmailChange(it) },
                label = { Text("Correo electrónico") },
                isError = registerViewModel.emailError.isNotEmpty()
            )

            if (registerViewModel.emailError.isNotEmpty()) {
                Box(modifier = Modifier.height(17.dp)) {
                    Text(
                        text = registerViewModel.emailError,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                value = registerViewModel.password,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { registerViewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                isError = registerViewModel.passwordError.isNotEmpty(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                }
            )

            if (registerViewModel.passwordError.isNotEmpty()) {
                Box(modifier = Modifier.height(17.dp)) {
                    Text(
                        text = registerViewModel.passwordError,
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
                onValueChange = { registerViewModel.onPasswordConfirmationChange(it) },
                label = { Text("Confirmar contraseña") },
                isError = registerViewModel.passwordConfirmationError.isNotEmpty(),
                visualTransformation = if (repeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { repeatPasswordVisible = !repeatPasswordVisible }) {
                        Icon(
                            imageVector = if (repeatPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                }
            )

            if (registerViewModel.passwordConfirmationError.isNotEmpty()) {
                Box(modifier = Modifier.height(17.dp)) {
                    Text(
                        text = registerViewModel.passwordConfirmationError,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Button(
            enabled = registerViewModel.validateForm(),
            onClick = { registerViewModel.register() },
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
fun RegisterPreview() {
    ProyectoFinalDisenoMovilTheme {
        RegisterScreen()
    }
}