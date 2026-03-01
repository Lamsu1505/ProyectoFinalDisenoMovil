package com.example.proyectofinaldisenomovil.features.login

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
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
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme

@Composable
fun LoginScreen(
        navController : NavController ,
        loginViewModel : LoginViewModel = viewModel()
        ) {

    val Mycontext = LocalContext.current


    Box(
        modifier = Modifier.
            fillMaxSize().
            background(MaterialTheme.colorScheme.background)
    ){
        LoginHeaderSection()

        val cardShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        Card(
            shape = cardShape,
            modifier = Modifier.fillMaxWidth().
                align(Alignment.TopCenter)
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
                verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = "Inicia Sesion",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy( 15.dp , Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {

                    OutlinedTextField(
                        singleLine = true,
                        maxLines = 1,
                        shape = RoundedCornerShape(15.dp),
                        value = loginViewModel.email,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = {
                            loginViewModel.onEmailChange(it)
                        },
                        label = { Text ("Usuario o Email") },
                        isError = loginViewModel.emailError.isNotEmpty(),
                        supportingText = {
                            if(loginViewModel.emailError.isNotEmpty()){
                                Text ("Escribe tu email")
                            }
                        }
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {

                        OutlinedTextField(
                            singleLine = true,
                            shape = RoundedCornerShape(15.dp),
                            value = loginViewModel.password,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = {
                                loginViewModel.onPasswordChange(it)
                            },
                            label = { Text("Contraseña") },
                            isError = loginViewModel.passwordError.isNotEmpty()
                        )

                        if (loginViewModel.passwordError.isNotEmpty()) {
                            Text(
                                text = "La contraseña debe tener al menos 8 caracteres",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }

                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .height(50.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )

                    ) {
                        Text(
                            text ="Iniciar Sesión",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.background
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(

                    )
                    {
                        Text(
                            text= "No tienes cuenta? ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text= "Creala Aqui",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            textDecoration = TextDecoration.Underline
                        )

                    }



                }




            }

        }

    }
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.spacedBy( 16.dp , Alignment.CenterVertically),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        OutlinedTextField(
//            value = loginViewModel.email,
//            onValueChange = {
//                if(!Patterns.EMAIL_ADDRESS.matcher(it).matches()){
//                    loginViewModel.emailError = "Error"
//                }
//                else{
//                    loginViewModel.emailError = ""
//                }
//                loginViewModel.email = it
//            },
//            label = { Text("Email") },
//            isError = loginViewModel.emailError.isNotEmpty(),
//            supportingText = {
//                if(loginViewModel.emailError.isNotEmpty()){
//                    Text ("Email mal escrito")
//                }
//            }
//
//        )
//
//        OutlinedTextField(
//            value = loginViewModel.password,
//            visualTransformation = PasswordVisualTransformation(),
//            onValueChange = {
//                loginViewModel.password = it
//             },
//            label = { Text("Contraseña") },
//        )
//
//        Button(
//            onClick = {
//                if(loginViewModel.login()){
//                    Toast.makeText(
//                        Mycontext,
//                        "Login Correcto",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    navController.navigate(AppScreens.FirstScreen.route)
//                }
//
//            }
//        ) {
//            Text("Iniciar Sesión")
//        }
//    }
}

@Composable
fun LoginHeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.40f)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally)
        {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de vive tu zona",
                modifier = Modifier.size(200.dp)
            )


            Text(
                text = "ViveTuZona",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    ProyectoFinalDisenoMovilTheme() {
        LoginScreen(navController = navController)
    }

}