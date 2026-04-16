package com.example.proyectofinaldisenomovil.core.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyectofinaldisenomovil.core.component.LanguageSelectorDialog
import com.example.proyectofinaldisenomovil.core.utils.LocaleViewModel

@Composable
fun SharedSettingsNavigation(
    localeViewModel: LocaleViewModel = hiltViewModel()
) {
    val currentLanguage by localeViewModel.currentLanguage.collectAsState()
    val context = LocalContext.current
}

@Composable
fun LanguageSelectorWrapper(
    showDialog: Boolean,
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        LanguageSelectorDialog(
            currentLanguage = currentLanguage,
            onLanguageSelected = onLanguageSelected,
            onDismiss = onDismiss
        )
    }
}

fun changeLanguageAndRecreate(
    activity: Activity?,
    languageCode: String,
    onLanguageChanged: (String) -> Unit
) {
    onLanguageChanged(languageCode)
    activity?.let { act ->
        val locale = java.util.Locale(languageCode)
        val config = android.content.res.Configuration(act.resources.configuration)
        config.setLocale(locale)
        act.resources.updateConfiguration(config, act.resources.displayMetrics)
        act.recreate()
    }
}
