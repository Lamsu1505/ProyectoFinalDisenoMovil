package com.example.proyectofinaldisenomovil

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.cloudinary.android.MediaManager
import com.example.proyectofinaldisenomovil.core.navigation.AppNavigation
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.data.local.SessionManager
import com.example.proyectofinaldisenomovil.data.local.SettingsDataStore
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    @Inject
    lateinit var sessionManager: SessionManager

    override fun attachBaseContext(newBase: Context) {
        val languageCode = runBlocking {
            try {
                settingsDataStore.languageFlow.first()
            } catch (e: Exception) {
                sessionManager.appLanguage.first()
            }
        }

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)

        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val configMap = mapOf(
            "cloud_name" to "dcr9hdiat",
            "api_key" to "716781964881746",
            "secure" to true
        )
        MediaManager.init(this, configMap)
        subirImagen()
        enableEdgeToEdge()

        setContent {
            val currentLanguage by settingsDataStore.languageFlow.collectAsState(initial = SettingsDataStore.DEFAULT_LANGUAGE)

            ProyectoFinalDisenoMovilTheme {
                Surface { AppNavigation() }
            }
        }
    }

    fun updateLanguage(languageCode: String) {
        runOnUiThread {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            val config = resources.configuration
            config.setLocale(locale)
            config.setLayoutDirection(locale)

            resources.updateConfiguration(config, resources.displayMetrics)

            recreate()
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
