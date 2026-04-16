package com.example.proyectofinaldisenomovil.features.loginFlow.login

import android.app.Activity
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.component.login.LoginHeaderSection
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.core.theme.green
import com.example.proyectofinaldisenomovil.domain.model.User.UserRole
import com.example.proyectofinaldisenomovil.features.settings.SettingsViewModel

@Composable
fun LoginScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    onNavigateToForgotPassword : () -> Unit,
    onNavigateToRegister : () -> Unit,
    onNavigateToModeratorFlow : () -> Unit,
    onNavigateToUserFlow : () -> Unit,
    onLoginSuccess: (String, com.example.proyectofinaldisenomovil.domain.model.UserRole) -> Unit = { _, _ -> }
) {
    val loginResult by loginViewModel.loginResult.collectAsState()
    val currentLanguage by settingsViewModel.currentLanguage.collectAsState()
    val context = LocalContext.current
    
    var showLanguageDialog by remember { mutableStateOf(false) }

    LaunchedEffect(loginResult) {
        when (val result = loginResult) {
            is LoginResult.Success -> {
                val mappedRole = when (result.role) {
                    UserRole.USER -> com.example.proyectofinaldisenomovil.domain.model.UserRole.USER
                    UserRole.MODERATOR -> com.example.proyectofinaldisenomovil.domain.model.UserRole.ADMIN
                }
                onLoginSuccess(result.userId, mappedRole)
                if (result.role == UserRole.MODERATOR) onNavigateToModeratorFlow()
                else onNavigateToUserFlow()
                loginViewModel.resetResult()
            }
            else -> Unit
        }
    }

    Scaffold(
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            LoginHeaderSection(
                onLanguageClick = { showLanguageDialog = true }
            )

            val cardShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            Card(
                shape = cardShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(5.dp)
                    .offset(y = 308.dp),
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
                    verticalArrangement = Arrangement.spacedBy(50.dp)
                ) {
                    LoginForm(
                        loginViewModel,
                        onNavigateToForgotPassword,
                        onNavigateToRegister
                    )
                }
            }
        }
    }

    if (showLanguageDialog) {
        LanguageLoginDialog(
            currentLanguage = currentLanguage,
            onLanguageSelected = { code ->
                settingsViewModel.setLanguage(code)
                showLanguageDialog = false
                (context as? Activity)?.let { activity ->
                    val locale = java.util.Locale(code)
                    val config = android.content.res.Configuration(context.resources.configuration)
                    config.setLocale(locale)
                    context.resources.updateConfiguration(config, context.resources.displayMetrics)
                    activity.recreate()
                }
            },
            onDismiss = { showLanguageDialog = false }
        )
    }
}


@Composable
fun LoginForm(
    loginViewModel: LoginViewModel,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToRegister: () -> Unit
){
    var passwordVisible by remember { mutableStateOf(false) }

    Text(
        text = stringResource(R.string.login_title),
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                value = loginViewModel.email,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { loginViewModel.onEmailChange(it) },
                label = { Text(stringResource(R.string.login_email)) },
                isError = loginViewModel.emailError.isNotEmpty()
            )

            Box(modifier = Modifier.height(17.dp)) {
                if (loginViewModel.emailError.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.validation_email_invalid),
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
                value = loginViewModel.password,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = { Text(stringResource(R.string.login_password)) },
                isError = loginViewModel.passwordError.isNotEmpty(),
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

            Box(modifier = Modifier.height(17.dp)) {
                if (loginViewModel.passwordError.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.validation_password_short),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }

            Text(
                text = stringResource(R.string.login_forgot_password),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.align(Alignment.End).clickable { onNavigateToForgotPassword() }
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            enabled = loginViewModel.validateForm(),
            onClick = { loginViewModel.login() },
            modifier = Modifier.height(50.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                disabledContentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(text = stringResource(R.string.login_button), fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Text(
                text = stringResource(R.string.label_no_account),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.label_create_here),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ProyectoFinalDisenoMovilTheme {
        LoginScreen(
            onNavigateToForgotPassword = {},
            onNavigateToRegister = {},
            onNavigateToModeratorFlow = {},
            onNavigateToUserFlow = {}
        )
    }
}

@Composable
fun LanguageLoginDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languages = listOf(
        Pair("es", "Español"),
        Pair("en", "English"),
    )

    val flagEmojis = mapOf(
        "es" to "\uD83C\uDDEA\uD83C\uDDF8",
        "en" to "\uD83C\uDDFA\uD83C\uDDF8"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.language_login_title),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                languages.forEach { (code, name) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(code) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = flagEmojis[code] ?: "", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = name, fontSize = 16.sp)
                        }
                        if (code == currentLanguage) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = green,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    if (code != languages.last().first) {
                        HorizontalDivider(color = androidx.compose.ui.graphics.Color.LightGray)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel), color = androidx.compose.ui.graphics.Color.Gray)
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = androidx.compose.ui.graphics.Color.White
    )
}
