package com.example.proyectofinaldisenomovil.features.RecoverPassword

import android.graphics.drawable.Icon
import android.graphics.pdf.content.PdfPageGotoLinkContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.component.login.TopBarRegister
import com.example.proyectofinaldisenomovil.core.navigation.AppScreens
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.features.home.HomeScreen
import com.example.proyectofinaldisenomovil.features.login.LoginScreen
import com.example.proyectofinaldisenomovil.features.login.RegisterViewModel


@Composable
fun RecoverPasswordScreen(
    navController: NavController,
    recoverPasswordViewModel: RegisterViewModel = viewModel()
) {

    Scaffold(
        topBar = {
            TopBarRegister(navController)
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier.padding(paddingValues)
        ) {

        }

    }

}

@Preview(showBackground = true)
@Composable
fun RecoverPasswordScreenPreview(){
    ProyectoFinalDisenoMovilTheme() {
        RecoverPasswordScreen(navController = rememberNavController())
    }
}

