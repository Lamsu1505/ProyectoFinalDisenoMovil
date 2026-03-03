package com.example.proyectofinaldisenomovil

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cloudinary.android.MediaManager
import com.example.proyectofinaldisenomovil.core.navigation.AppNavigation
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        //Configuracion de CLoudinary
        val config = mapOf(
            "cloud_name" to "dcr9hdiat",
            "api_key" to "716781964881746",
            "secure" to true
        )
        MediaManager.init(this, config)
        subirImagen()

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

fun drawableToFile(context: Context, drawableId: Int): File {
    val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)
    val file = File(context.cacheDir, "temp_image.png")

    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    return file
}

fun subirImagen(){
    //TODO

}