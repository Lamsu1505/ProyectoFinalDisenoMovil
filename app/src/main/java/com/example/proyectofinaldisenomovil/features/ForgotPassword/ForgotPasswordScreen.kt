package com.example.proyectofinaldisenomovil.features.ForgotPassword

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.component.login.HeaderSectionNonLogued
import com.example.proyectofinaldisenomovil.core.component.login.LoginHeaderSection
import com.example.proyectofinaldisenomovil.core.component.login.TopBarRegister
import com.example.proyectofinaldisenomovil.core.navigation.AppScreens
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.features.login.LoginForm

@Composable
fun ForgotPasswordScreen (
    navController: NavController,
    forgotPasswordViewModel: ForgotPasswordViewModel = viewModel()
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
                .background(MaterialTheme.colorScheme.background)
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
                        .padding(horizontal = 35.dp, vertical = 40.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    ForgotPasswordForm(
                        navController,
                        forgotPasswordViewModel,
                        myContext
                    )
                }
            }
        }



    }
}


@Composable
fun ForgotPasswordForm(
    navController: NavController,
    forgotPasswordViewModel: ForgotPasswordViewModel,
    myContext : Context
){
    Text(
        text = "Recupera tu Contraseña",
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        lineHeight = 40.sp,
        textAlign = TextAlign.Center
    )

    Text(
        text = "Ingresa el correo asosicado a tu cuenta y recibe un codigo para cambiar tu contraseña",
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

        // ---------------- EMAIL ----------------

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                value = forgotPasswordViewModel.email,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    forgotPasswordViewModel.onEmailChange(it)
                },
                label = { Text("Email") },
                isError = forgotPasswordViewModel.emailError.isNotEmpty()
            )

            Box(modifier = Modifier.height(17.dp)) {
                if (forgotPasswordViewModel.emailError.isNotEmpty()) {
                    Text(
                        text = "Email mal escrito, revisa el formato",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            enabled = forgotPasswordViewModel.validateForm(),
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
                text = "Enviar codigo",
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Regresar a Iniciar sesion",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                navController.navigate(AppScreens.LoginScreen.route)
            }
        )


    }

}


@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreen() {
    ProyectoFinalDisenoMovilTheme() {
        ForgotPasswordScreen(navController = rememberNavController())
    }

}


