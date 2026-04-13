package com.example.proyectofinaldisenomovil.features.loginFlow.RecoverPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppSnackbarHost
import com.example.proyectofinaldisenomovil.core.component.barReusable.SnackbarController
import com.example.proyectofinaldisenomovil.core.component.login.HeaderSectionNonLogued
import com.example.proyectofinaldisenomovil.core.component.login.TopBarRegister
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme

@Composable
fun RecoverPasswordScreen(
    email: String = "",
    onBackClick: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    var code by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var repeatPasswordVisible by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarController = remember {
        SnackbarController(scope, snackbarHostState)
    }

    val passwordMatchMessage = stringResource(R.string.validation_passwords_not_match)
    val passwordChangedMessage = stringResource(R.string.recover_password_success)

    Scaffold(
        topBar = {
            TopBarRegister(onBackClick)
        },
        snackbarHost = {
            AppSnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
                        .padding(horizontal = 35.dp, vertical = 40.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    Text(
                        text = stringResource(R.string.recover_password_new_title),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        lineHeight = 40.sp,
                        textAlign = TextAlign.Center
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = code,
                            onValueChange = { code = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp),
                            label = { Text(stringResource(R.string.recover_password_code)) },
                            maxLines = 1
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp),
                            label = { Text(stringResource(R.string.register_password_field)) },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = stringResource(R.string.label_password_visibility)
                                    )
                                }
                            }
                        )

                        OutlinedTextField(
                            value = repeatPassword,
                            onValueChange = { repeatPassword = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp),
                            label = { Text(stringResource(R.string.register_confirm_password_field)) },
                            visualTransformation = if (repeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { repeatPasswordVisible = !repeatPasswordVisible }) {
                                    Icon(
                                        imageVector = if (repeatPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = stringResource(R.string.label_password_visibility)
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (password == repeatPassword && password.isNotEmpty()) {
                                    snackbarController.showMessage(passwordChangedMessage)
                                    onSubmit()
                                } else {
                                    snackbarController.showMessage(passwordMatchMessage)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text(text = stringResource(R.string.recover_password_continue), fontSize = 20.sp)
                        }

                        Text(
                            text = stringResource(R.string.forgot_password_back_login),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                onNavigateToLogin()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecoverPasswordScreenPreview() {
    ProyectoFinalDisenoMovilTheme {
        RecoverPasswordScreen(
            onBackClick = {},
            onNavigateToLogin = {},
            onSubmit = {}
        )
    }
}