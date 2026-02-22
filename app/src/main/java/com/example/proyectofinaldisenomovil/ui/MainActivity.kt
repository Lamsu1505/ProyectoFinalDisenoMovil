package com.example.proyectofinaldisenomovil.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyectofinaldisenomovil.ui.navigation.AppNavigation
import com.example.proyectofinaldisenomovil.ui.theme.ProyectoFinalDisenoMovilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoFinalDisenoMovilTheme {
                AppNavigation()
            }
        }
    }
}



@Preview (showBackground = true)
@Composable
fun DefaultPreview(){
    ProyectoFinalDisenoMovilTheme {
        AppNavigation()
    }
}