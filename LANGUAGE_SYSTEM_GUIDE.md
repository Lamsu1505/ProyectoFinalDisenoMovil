# Sistema de Cambio de Idioma - ViveTuZona

## Descripción General

Este sistema permite cambiar el idioma de la aplicación (Español/Inglés) de manera persistente usando Preferences Data Store, con actualización global de la interfaz.

## Arquitectura del Sistema

### 1. SettingsDataStore (`data/local/SettingsDataStore.kt`)

Gestiona el almacenamiento persistente del código de idioma.

**Características:**
- Usa Preferences Data Store para guardar `LANGUAGE_CODE`
- Expone `languageFlow: Flow<String>` para observar cambios
- Función suspend `setLanguage()` para guardar el idioma

```kotlin
@Singleton
class SettingsDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val LANGUAGE_CODE = stringPreferencesKey("language_code")
        const val DEFAULT_LANGUAGE = "es"
    }

    val languageFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[LANGUAGE_CODE] ?: DEFAULT_LANGUAGE
    }

    suspend fun setLanguage(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_CODE] = languageCode
        }
    }
}
```

### 2. ResourceProvider (`core/utils/ResourceProvider.kt`)

Interfaz para que los ViewModels accedan a strings sin depender del contexto de UI.

```kotlin
interface ResourceProvider {
    fun getString(@StringRes resId: Int): String
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String
}

@Singleton
class ResourceProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceProvider { ... }
```

### 3. SettingsViewModel (`features/settings/SettingsViewModel.kt`)

ViewModel global para gestionar el estado del idioma.

```kotlin
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val currentLanguage: StateFlow<String> = settingsDataStore.languageFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsDataStore.DEFAULT_LANGUAGE
        )

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            settingsDataStore.setLanguage(languageCode)
        }
    }
}
```

### 4. LocaleHelper (`core/utils/LocaleHelper.kt`)

Utilidades para cambiar el Locale global de la aplicación.

## Integración en Pantallas

### LoginScreen

```kotlin
@Composable
fun LoginScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    // ... otros parámetros
) {
    val currentLanguage by settingsViewModel.currentLanguage.collectAsState()
    val context = LocalContext.current
    var showLanguageDialog by remember { mutableStateOf(false) }

    // En el diálogo de idioma:
    LanguageLoginDialog(
        currentLanguage = currentLanguage,
        onLanguageSelected = { code ->
            settingsViewModel.setLanguage(code)
            // Aplicar cambio de Locale y recrear Activity
            (context as? Activity)?.let { activity ->
                val locale = Locale(code)
                val config = Configuration(context.resources.configuration)
                config.setLocale(locale)
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
                activity.recreate()
            }
        },
        onDismiss = { showLanguageDialog = false }
    )
}
```

### ProfileScreen

```kotlin
@Composable
fun ProfileScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    // ... otros parámetros
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val currentLanguage by settingsViewModel.currentLanguage.collectAsState()
    val context = LocalContext.current

    // En el diálogo de idioma:
    LanguageChangeDialog(
        currentLanguage = currentLanguage,
        onLanguageSelected = { code ->
            settingsViewModel.setLanguage(code)
            profileViewModel.setLanguage(code) // Para mensajes localizados
            (context as? Activity)?.let { activity ->
                // Aplicar cambio de Locale y recrear
            }
        },
        onDismiss = { showLanguageDialog = false }
    )
}
```

## ProfileViewModel con ResourceProvider

El ProfileViewModel usa ResourceProvider para obtener mensajes localizados:

```kotlin
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val currentLanguage: StateFlow<String> = settingsDataStore.languageFlow
        .stateIn(...)

    fun loadUserProfile() {
        viewModelScope.launch {
            val user = MockDataRepository.getCurrentUser()
            if (user != null) {
                _uiState.value = _uiState.value.copy(
                    successMessage = resourceProvider.getString(R.string.profile_success_load)
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = resourceProvider.getString(R.string.profile_error_load)
                )
            }
        }
    }
}
```

## Navegación por Rol y Cambio de Idioma

La navegación por rol (USER/ADMIN) se mantiene independientemente del cambio de idioma:

### AppNavigation.kt

```kotlin
@Composable
fun MainNavigation(
    session: UserSession,
    onLogout: () -> Unit
) {
    when (session.role) {
        UserRole.USER -> UserNavigation(onLogout = onLogout)
        UserRole.ADMIN -> ModeratorNavigation(onLogout = onLogout)
    }
}
```

### Flujo de Cambio de Idioma

1. Usuario abre diálogo de idioma en Login o Profile
2. Selectiona nuevo idioma (es/en)
3. SettingsDataStore guarda el código persistentemente
4. Se actualiza el Locale globalmente
5. Se recrea la Activity
6. `attachBaseContext()` en MainActivity lee el idioma guardado
7. Toda la UI se renderiza en el nuevo idioma
8. La navegación por rol (USER/ADMIN) se mantiene intacta

## Archivos de Strings

- `res/values/strings.xml` - Español (default)
- `res/values-en/strings.xml` - Inglés

### Strings Agregados

```xml
<!-- Language Settings -->
<string name="language_spanish">Español</string>
<string name="language_english">English</string>
<string name="language_change_success">Idioma cambiado correctamente</string>
<string name="language_current">Idioma actual</string>
<string name="language_login_title">Cambiar idioma</string>

<!-- Profile Messages -->
<string name="profile_success_load">Perfil cargado correctamente</string>
<string name="profile_error_load">Error al cargar el perfil</string>
<string name="profile_save_success">Cambios guardados</string>
<string name="profile_save_error">Error al guardar los cambios</string>
```

## Dependencias Requeridas

```kotlin
// DataStore
implementation(libs.datastore.preferences)

// Hilt
implementation(libs.hilt.android)
ksp(libs.hilt.compiler)
implementation(libs.hilt.navigation.compose)
```

## Inyección de Hilt

### AppModule.kt

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSettingsDataStore(
        dataStore: DataStore<Preferences>
    ): SettingsDataStore {
        return SettingsDataStore(dataStore)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ResourceModule {

    @Binds
    @Singleton
    abstract fun bindResourceProvider(
        resourceProviderImpl: ResourceProviderImpl
    ): ResourceProvider
}
```

## Notas Importantes

1. **Persistencia**: El idioma se guarda en DataStore, sobreviviendo a reinicios de la app
2. **Reactividad**: Los cambios en el Flow<String> actualizan automáticamente la UI
3. **Seguridad**: ResourceProvider permite acceso a strings desde ViewModels sin filtrar contexto
4. **Compatibilidad**: El cambio de Locale funciona en todas las versiones de Android 6.0+
5. **Rol Preservado**: El cambio de idioma NO afecta la navegación USER/ADMIN
