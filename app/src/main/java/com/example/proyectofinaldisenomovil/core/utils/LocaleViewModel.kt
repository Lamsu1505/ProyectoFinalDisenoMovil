package com.example.proyectofinaldisenomovil.core.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.local.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LocaleViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val currentLanguage: StateFlow<String> = settingsDataStore.languageFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsDataStore.DEFAULT_LANGUAGE
        )

    fun setLanguage(languageCode: String, activity: Activity?) {
        viewModelScope.launch {
            settingsDataStore.setLanguage(languageCode)
            applyLanguage(languageCode)
            activity?.recreate()
        }
    }

    private fun applyLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun getLanguageDisplayName(languageCode: String): String {
        return when (languageCode) {
            "es" -> "Español"
            "en" -> "English"
            else -> languageCode
        }
    }

    fun getAvailableLanguages(): List<Pair<String, String>> {
        return listOf(
            Pair("es", "Español"),
            Pair("en", "English")
        )
    }
}
