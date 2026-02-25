package com.example.proyectofinaldisenomovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyectofinaldisenomovil.core.navigation.AppNavigation
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoFinalDisenoMovilTheme {
                Surface{AppNavigation()}

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