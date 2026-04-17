package com.example.proyectofinaldisenomovil.features.loginFlow.RecoverPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppSnackbarHost
import com.example.proyectofinaldisenomovil.core.component.barReusable.SnackbarController
import com.example.proyectofinaldisenomovil.core.component.login.HeaderSectionNonLogued
import com.example.proyectofinaldisenomovil.core.component.login.TopBarRegister
import com.example.proyectofinaldisenomovil.core.utils.RequestResult

@Composable
fun RecoverPasswordScreen(
    email: String = "",
    sentCode: String = "",
    onBackClick: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onPasswordResetSuccess: () -> Unit = {},
    recoverPasswordViewModel: RecoverPasswordViewModel = hiltViewModel()
) {
    val codeDigits by recoverPasswordViewModel.codeDigits.collectAsState()
    val newPasswordField by recoverPasswordViewModel.newPasswordField.collectAsState()
    val confirmPasswordField by recoverPasswordViewModel.confirmPasswordField.collectAsState()
    val requestResult by recoverPasswordViewModel.requestResult.collectAsState()
    val isPasswordVisible by recoverPasswordViewModel.isPasswordVisible.collectAsState()
    val isConfirmPasswordVisible by recoverPasswordViewModel.isConfirmPasswordVisible.collectAsState()

    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarController = remember { SnackbarController(scope, snackbarHostState) }

    LaunchedEffect(Unit) {
        recoverPasswordViewModel.initialize(email, sentCode)
    }

    LaunchedEffect(requestResult) {
        when (val result = requestResult) {
            is RequestResult.Success -> {
                snackbarController.showMessage(result.message)
                onPasswordResetSuccess()
                recoverPasswordViewModel.resetState()
            }
            is RequestResult.Failure -> {
                snackbarController.showMessage(result.errorMessage)
                recoverPasswordViewModel.resetState()
            }
            is RequestResult.Loading -> {}
            null -> {}
        }
    }

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
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.recover_password_new_title),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        lineHeight = 40.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(R.string.recover_password_enter_code),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    VerificationCodeInput(
                        codeDigits = codeDigits,
                        onDigitChange = recoverPasswordViewModel::onDigitChange,
                        isLoading = requestResult is RequestResult.Loading
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = newPasswordField.value,
                            onValueChange = recoverPasswordViewModel::onNewPasswordChange,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp),
                            label = { Text(stringResource(R.string.register_password_field)) },
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { recoverPasswordViewModel.togglePasswordVisibility() }) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = stringResource(R.string.label_password_visibility)
                                    )
                                }
                            },
                            isError = newPasswordField.error != null,
                            enabled = requestResult !is RequestResult.Loading,
                            singleLine = true
                        )

                        if (newPasswordField.error != null) {
                            Text(
                                text = newPasswordField.error!!,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        OutlinedTextField(
                            value = confirmPasswordField.value,
                            onValueChange = recoverPasswordViewModel::onConfirmPasswordChange,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp),
                            label = { Text(stringResource(R.string.register_confirm_password_field)) },
                            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { recoverPasswordViewModel.toggleConfirmPasswordVisibility() }) {
                                    Icon(
                                        imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = stringResource(R.string.label_password_visibility)
                                    )
                                }
                            },
                            isError = confirmPasswordField.error != null,
                            enabled = requestResult !is RequestResult.Loading,
                            singleLine = true
                        )

                        if (confirmPasswordField.error != null) {
                            Text(
                                text = confirmPasswordField.error!!,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { recoverPasswordViewModel.resetPassword() },
                        enabled = recoverPasswordViewModel.isFormValid() && requestResult !is RequestResult.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        if (requestResult is RequestResult.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onSecondary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.recover_password_continue),
                                fontSize = 20.sp
                            )
                        }
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

@Composable
fun VerificationCodeInput(
    codeDigits: List<CodeDigitField>,
    onDigitChange: (Int, String) -> Unit,
    isLoading: Boolean
) {
    val focusRequesters = remember { List(5) { FocusRequester() } }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        codeDigits.forEachIndexed { index, digitField ->
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(56.dp)
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                OutlinedTextField(
                    value = digitField.value,
                    onValueChange = { onDigitChange(index, it) },
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRequester(focusRequesters[index]),
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    enabled = !isLoading,
                    isError = digitField.isError,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        errorBorderColor = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    }
}
