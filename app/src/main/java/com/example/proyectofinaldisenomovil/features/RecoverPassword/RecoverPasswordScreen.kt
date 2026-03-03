package com.example.proyectofinaldisenomovil.features.RecoverPassword

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme

@Composable
fun RecoverPasswordScreen(
    NavController: NavController,
    recoverPasswordViewModel: RecoverPasswordViewModel = viewModel()
) {
}

@Preview(showBackground = true)
@Composable
fun RecoverPasswordScreenPreview(){
    ProyectoFinalDisenoMovilTheme() {
        RecoverPasswordScreen(NavController = rememberNavController())
    }
}

