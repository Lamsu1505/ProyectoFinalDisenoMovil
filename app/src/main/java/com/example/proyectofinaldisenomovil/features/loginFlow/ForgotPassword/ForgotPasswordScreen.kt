package com.example.proyectofinaldisenomovil.features.loginFlow.ForgotPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppSnackbarHost
import com.example.proyectofinaldisenomovil.core.component.barReusable.SnackbarController
import com.example.proyectofinaldisenomovil.core.component.login.HeaderSectionNonLogued
import com.example.proyectofinaldisenomovil.core.component.login.TopBarRegister
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.core.utils.RequestResult
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onNavigateToRecoverPassword: (String, String) -> Unit = { _, _ -> },
    onNavigateToLogin: () -> Unit = {}
) {
    val emailField by forgotPasswordViewModel.emailField.collectAsState()
    val requestResult by forgotPasswordViewModel.requestResult.collectAsState()

    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarController = remember { SnackbarController(scope, snackbarHostState) }

    LaunchedEffect(requestResult) {
        when (val result = requestResult) {
            is RequestResult.Success -> {
                snackbarController.showMessage(result.message)
                onNavigateToRecoverPassword(
                    forgotPasswordViewModel.getTargetEmail(),
                    forgotPasswordViewModel.getSentCode()
                )
                forgotPasswordViewModel.resetState()
            }
            is RequestResult.Failure -> {
                snackbarController.showMessage(result.errorMessage)
                forgotPasswordViewModel.resetState()
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
                    verticalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    ForgotPasswordForm(
                        emailField = emailField,
                        isLoading = requestResult is RequestResult.Loading,
                        onEmailChange = forgotPasswordViewModel::onEmailChange,
                        onSendCode = forgotPasswordViewModel::sendRecoveryCode,
                        onNavigateToLogin = onNavigateToLogin
                    )
                }
            }
        }
    }
}


@Composable
fun ForgotPasswordForm(
    emailField: com.example.proyectofinaldisenomovil.core.utils.ValidatedField,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onSendCode: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Text(
        text = stringResource(R.string.forgot_password_title),
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        lineHeight = 40.sp,
        textAlign = TextAlign.Center
    )

    Text(
        text = stringResource(R.string.forgot_password_description),
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface,
        lineHeight = 20.sp,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(30.dp))

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                value = emailField.value,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = onEmailChange,
                label = { Text(stringResource(R.string.login_email)) },
                isError = emailField.error != null,
                enabled = !isLoading
            )

            Box(modifier = Modifier.height(17.dp)) {
                if (emailField.error != null) {
                    Text(
                        text = emailField.error!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onSendCode,
            enabled = emailField.isValid && !isLoading,
            modifier = Modifier.height(50.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                disabledContentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onSecondary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(R.string.forgot_password_send_code),
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

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
