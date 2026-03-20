package com.example.proyectofinaldisenomovil.features.loginFlow.login

import android.content.Context
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.component.login.LoginHeaderSection
import com.example.proyectofinaldisenomovil.core.navigation.UserRoutes
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.data.model.User.UserRole

@Composable
fun LoginScreen(
        loginViewModel: LoginViewModel = viewModel(),
        onNavigateToForgotPassword : () -> Unit = {},
        onNavigateToRegister : () -> Unit = {},
        onLoginSuccess: (UserRole) -> Unit
) {

    val myContext = LocalContext.current

//    LaunchedEffect(loginResult) {
//        if (loginResult is LoginResult.Success) {
//            viewModel.resetResult()
//            onLoginSuccess((loginResult as LoginResult.Success).role)  // pasa el rol
//        }
//    }

    Scaffold(
    ) {
        paddingValues ->

        //Caja principal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {

            //Componente de la carpeta core
            LoginHeaderSection()

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
                        myContext,
                        onNavigateToForgotPassword,
                        onNavigateToRegister
                    )
                }
            }
        }
    }
}


@Composable
fun LoginForm(
    loginViewModel: LoginViewModel,
    myContext : Context,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToRegister: () -> Unit
){

    var passwordVisible by remember { mutableStateOf(false) }

    Text(
        text = "Inicia Sesion",
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )

    //Formulario de login
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ---------------- EMAIL ----------------

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                value = loginViewModel.email,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    loginViewModel.onEmailChange(it)
                },
                label = { Text("Email") },
                isError = loginViewModel.emailError.isNotEmpty()
            )

            Box(modifier = Modifier.height(17.dp)) {
                if (loginViewModel.emailError.isNotEmpty()) {
                    Text(
                        text = "Email mal escrito, revisa el formato",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // ---------------- PASSWORD ----------------

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                value = loginViewModel.password,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    loginViewModel.onPasswordChange(it)
                },
                label = { Text("Contraseña") },
                isError = loginViewModel.passwordError.isNotEmpty(),
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

            Box(modifier = Modifier.height(17.dp)) {
                if (loginViewModel.passwordError.isNotEmpty()) {
                    Text(
                        text = loginViewModel.passwordError,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }

            Text(
                text = "¿Olvidaste tu contraseña?",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.align(Alignment.End).
                    clickable( onClick = {
                        onNavigateToForgotPassword()
                    })
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            enabled = loginViewModel.validateForm(),
            onClick = {
//                loginViewModel.login { success ->
//                    if (success) {
//                        navController.navigate(AppScreens.HomeScreen.route)
//                    } else {
//                        Toast.makeText(myContext, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
//                    }
//                }
            },
            modifier = Modifier.height(50.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                disabledContentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(
                text = "Iniciar Sesión",
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Text(
                text = "No tienes cuenta? ",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Creala Aqui",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    onNavigateToRegister()
                }
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ProyectoFinalDisenoMovilTheme {
        LoginScreen(
            onLoginSuccess = {}
        )
    }
}