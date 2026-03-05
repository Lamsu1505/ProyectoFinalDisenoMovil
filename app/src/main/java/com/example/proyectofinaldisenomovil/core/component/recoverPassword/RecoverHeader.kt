package com.example.proyectofinaldisenomovil.core.component.recoverPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme

@Composable
fun RecoverHeader(
    navController: NavController
) {

    Box(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .height(420.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {

        Column(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.mipmap.logo),
                contentDescription = "Logo",
                modifier = Modifier.Companion.size(220.dp)
            )

            Spacer(modifier = Modifier.Companion.height(16.dp))

            Text(
                text = "ViveTuZona",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.Companion.height(8.dp))

            Text(
                text = "Cambia tu contraseña",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Companion.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreen(
) {
    ProyectoFinalDisenoMovilTheme() {
        RecoverHeader(navController = rememberNavController())
    }

}