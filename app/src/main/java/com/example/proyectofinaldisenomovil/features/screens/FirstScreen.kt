package com.example.proyectofinaldisenomovil.features.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.navigation.AppScreens
import androidx.compose.material3.*
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary),
                title = {
                    Text("A")
                }

            )
        }
    ) { innerPadding ->

        BodyContent(
            navController = navController,
            innerPadding = innerPadding
        )
    }
}

@Composable
fun BodyContent(
    navController: NavController,
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Navegacion pantalla 1")

        Button(
            onClick = {
                navController.navigate(AppScreens.LoginScreen.route)
            }
        ) {
            Text("Presioname")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FirstScreenPreview() {

    ProyectoFinalDisenoMovilTheme() {
        val navController = rememberNavController()
        FirstScreen(navController = navController)
    }

}