# ProyectoFinalDisenoMovil

Aplicación móvil para ViveTuZona con soporte multiidioma, navegación basada en roles y arquitectura moderna.

## 📱 Descripción del Proyecto

ProyectoFinalDisenoMovil es una aplicación Android desarrollada como trabajo final para la asignatura de Diseño Móvil. La aplicación permite a los usuarios de ViveTuZona:

- Iniciar sesión y gestionar su perfil
- Cambiar el idioma de la aplicación entre Español e Inglés de forma persistente
- Navegar según su rol (usuario estándar o moderador)
- Acceder a funcionalidades específicas según sus permisos

La aplicación utiliza las últimas tecnologías de Android Jetpack y sigue las mejores prácticas de arquitectura limpia.

## ✨ Características Principales

### 🌐 Sistema Multiidioma Avanzado
- Cambio de idioma en tiempo real entre Español (es) e Inglés (en)
- Persistencia del idioma seleccionado usando Preferences Data Store
- Actualización global de la interfaz sin reiniciar la aplicación
- Soporte para ViewModels accesando strings mediante ResourceProvider
- Documentación detallada en [LANGUAGE_SYSTEM_GUIDE.md](./LANGUAGE_SYSTEM_GUIDE.md)

### 👥 Navegación por Roles
- Experiencia distinta para usuarios estándar (USER) y moderadores (ADMIN)
- Mantiene el estado de rol independientemente del cambio de idioma
- Implementación usando Jetpack Compose Navigation con argumentos seguros

### ⚙️ Arquitectura Moderna
- **MVVM** con ViewModels y StateFlow/Flow para manejo de estado reactivo
- **Inyección de Dependencias** con Hilt para desacoplamiento y testabilidad
- **Jetpack Compose** para UI declarativa y reactiva
- **Preferences Data Store** para almacenamiento seguro de preferencias
- Separación clara de capas: presentación, dominio y datos

### ☁️ Integración con Firebase
- Autenticación de usuarios con Firebase Auth
- Almacenamiento de datos en Cloud Firestore
- Funciones en la nube para lógica de backend
- Configuración mediante archivo google-services.json
- Funciones desplegadas en Firebase Cloud Functions (en carpeta `/functions`)

### 🛡️ Buenas Prácticas Implementadas
- Manejo adecuado del ciclo de vida con viewModelScope
- Uso de corrutinas para operaciones asíncronas
- Principios SOLID y código limpio
- Recursos organizados y traducidos (strings.xml)
- ProGuard configurado para release
- Manejo adecuado de contexto y recursos

## 🏗️ Arquitectura del Proyecto

```
ProyectoFinalDisenoMovil/
├── app/                     # Módulo principal de la aplicación Android
│   ├── src/
│   │   └── main/
│   │       ├── java/        # Código Kotlin organizado por paquetes
│   │       │   ├── presentation/   # Capa de UI (Jetpack Compose)
│   │       │   │   ├── screens/    # Pantallas de la aplicación
│   │       │   │   ├── components/ # Componentes reutilizables
│   │       │   │   └── theme/      # Tema Material You
│   │       │   ├── domain/         # Casos de uso y entidades de negocio
│   │       │   └── data/           # Capa de datos (repositorios, fuentes)
│   │       └── res/              # Recursos (layouts, strings, drawables, etc.)
│   ├── build.gradle.kts          # Configuración de compilación del módulo app
│   └── google-services.json      # Configuración de Firebase
├── core/                    # Módulo de código compartido entre módulos
│   ├── utils/               # Utilidades transversales (LocaleHelper, ResourceProvider)
│   ├── navigation/          # Configuración de navegación por rol y lenguaje
│   └── di/                  # Módulos de inyección de dependencias (Hilt)
├── functions/               # Firebase Cloud Functions (TypeScript/JavaScript)
│   ├── src/                 # Código fuente de las funciones
│   ├── package.json         # Dependencias de Node.js
│   └── index.js             # Punto de entrada de las funciones
├── build.gradle.kts         # Configuración de nivel superior (plugins comunes)
├── settings.gradle.kts      # Definición de módulos incluidos en el build
├── gradle.properties        # Propiedades de configuración de Gradle
├── local.properties         # Configuración local del SDK de Android (no versionado)
└── firebase.json            # Configuración de Firebase Hosting y Functions
```

### Detalles de Implementación Clave

#### Sistema de Idiomas (Detallado en LANGUAGE_SYSTEM_GUIDE.md)
1. **SettingsDataStore**: Clase singleton que usa Preferences Data Store para guardar y observar el código de idioma
2. **ResourceProvider**: Interfaz que permite a los ViewModels acceder a strings sin depender de Android Context
3. **SettingsViewModel**: ViewModel global que expone el idioma actual como StateFlow
4. **LocaleHelper**: Utilidad para cambiar el Locale global y recrear la Activity cuando cambia el idioma
5. **Integración en UI**: Diálogos de selección de idioma en LoginScreen y ProfileScreen que actualizan el ViewModel y aplican el cambio de Locale

#### Navegación por Roles
- Definida en `core/navigation/UserRoutes.kt`
- La función `MainNavigation()` dirige a diferentes navegaciones basado en el rol del usuario:
  - `UserNavigation()` para rol USER
  - `ModeratorNavigation()` para rol ADMIN
- El rol se mantiene en `UserSession` y no se pierde al cambiar idioma

## 🛠️ Tecnologías Utilizadas

### Lenguaje y Frameworks
- **Kotlin 1.8+**: Lenguaje principal de programación
- **Jetpack Compose**: Framework moderno para UI declarativa
- **Android Architecture Components**: ViewModel, StateFlow, DataStore
- **Hilt**: Inyección de dependencias para Android
- **Jetpack Navigation**: Navegación entre composables

### Firebase y Servicios en la Nube
- **Firebase Authentication**: Gestión de usuarios
- **Cloud Firestore**: Base de datos NoSQL en tiempo real
- **Firebase Cloud Functions**: Lógica de backend ejecutable en la nube
- **Firebase Config**: Integración mediante google-services.json

### Herramientas de Desarrollo
- **Android Studio**: IDE oficial para desarrollo Android
- **Gradle con Kotlin DSL**: Sistema de construcción
- **Git**: Control de versiones
- **Material You**: Sistema de diseño de Android 12+

### Dependencias Destacadas
```kotlin
// Jetpack Compose
implementation(libs.bom.compose.material3)
implementation(libs.androidx.activity.compose)
implementation(libs.androidx.ui.tooling.preview)

// Arquitectura
implementation(libs.androidx.lifecycle.viewmodel.compose)
implementation(libs.datastore.preferences)
implementation(libs.androidx.lifecycle.runtime.ktx)

// Hilt
implementation(libs.hilt.android)
ksp(libs.hilt.compiler)
implementation(libs.hilt.navigation.compose)

// Firebase
implementation(libs.firebase.bom)
implementation(libs.firebase.auth)
implementation(libs.firebase.firestore)
```

## 🚀 Cómo Ejecutar el Proyecto

### Requisitos Previos
- **Android Studio** Flamingo o superior
- **JDK 11** o superior
- **Android SDK** 34 (API 34) o superior
- **Node.js** 16.x o superior (para funciones en la nube)
- Cuenta en **Firebase Console**

### Paso a Paso

#### 1. Clonar el Repositorio
```bash
git clone https://github.com/tu-usuario/ProyectoFinalDisenoMovil.git
cd ProyectoFinalDisenoMovil
```

#### 2. Configurar Firebase
1. Crear un proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Habilitar Authentication (método: Email/Password)
3. Habilitar Cloud Firestore (modo de prueba o producción)
4. Descargar el archivo `google-services.json` y colocarlo en:
   ```
   ProyectoFinalDisenoMovil/app/google-services.json
   ```

#### 3. Desplegar Funciones en la Nube
```bash
cd functions
npm install
# Configurar Firebase CLI si no está instalado
# npm install -g firebase-tools
firebase login
firebase init functions  # Seleccionar el proyecto creado
# Reemplazar el contenido de functions/index.js con el proporcionado
firebase deploy --only functions
```

#### 4. Ejecutar la Aplicación Android
1. Abrir el proyecto en Android Studio
2. Esperar a que Gradle sincronice todas las dependencias
3. Seleccionar un dispositivo (emulador o físico con API 23+)
4. Hacer clic en "Run" o presionar Shift+F10

#### 5. Primera Ejecución
- La aplicación iniciará en la pantalla de login
- Puede crear una nueva cuenta usando Firebase Auth
- Al iniciar sesión, será dirigido a la navegación según su rol (por defecto USER)
- Puede cambiar el idioma desde el perfil o la pantalla de login

## 📖 Documentación Adicional

- [LANGUAGE_SYSTEM_GUIDE.md](./LANGUAGE_SYSTEM_GUIDE.md): Guía detallada del sistema de cambio de idioma implementado
- Comentarios en el código: La mayoría de las clases y funciones tienen documentación KDoc
- Estructura de paquetes: Organizada por características y capas para fácil navegación

## 🤝 Contribuir

¡Gracias por considerar contribuir a este proyecto! Para hacerlo:

1. **Hacer Fork** del repositorio
2. **Crear una rama** para tu feature: `git checkout -b feature/nueva-caracteristica`
3. **Realizar tus cambios** siguiendo el estilo de código existente
4. **Commit tus cambios**: `git commit -m "feat: agregar nueva característica"`
5. **Push a tu rama**: `git push origin feature/nueva-caracteristica`
6. **Abrir un Pull Request** describiendo claramente tus cambios

### Guía de Estilo
- Seguir las convenciones de Kotlin [official coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Mantener funciones pequeñas y enfocadas
- Usar nombres descriptivos para variables y funciones
- Comentar el "por qué" no el "qué" cuando el código no sea obvio
- Mantener los archivos de recursos organizados y traducidos

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

## 🙏 Agradecimientos

- Equipo de Firebase por proporcionar una plataforma completa para desarrollo móvil
- Equipo de Android Jetpack por las bibliotecas modernas y bien diseñadas
- Comunidad open source por las innumerables bibliotecas y herramientas utilizadas
- Profesores y compañeros por su retroalimentación durante el desarrollo
- Todos los contribuidores que han ayudado a mejorar este proyecto

---

*Última actualización: Mayo 2026*  
*Desarrollado como trabajo final de Diseño Móvil*  
*© 2026 ProyectoFinalDisenoMovil. Todos los derechos reservados.*
