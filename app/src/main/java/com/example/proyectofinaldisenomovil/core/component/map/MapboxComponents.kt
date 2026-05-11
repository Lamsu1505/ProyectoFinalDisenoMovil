package com.example.proyectofinaldisenomovil.core.component.map

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Location
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapBoxMap
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.Annotation
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.LocationPlugin
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun MapboxViewer(
    modifier: Modifier = Modifier,
    events: List<Event> = emptyList(),
    userLocation: Location? = null,
    onUserLocationObtained: (Double, Double) -> Unit = { _, _ -> },
    onEventClick: (String) -> Unit = {},
    initialCameraPosition: CameraOptions = CameraOptions.Builder()
        .center(CoordinateDefaults.ARMENIA_COLOMBIA)
        .zoom(CoordinateDefaults.DEFAULT_ZOOM)
        .build()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var pointAnnotationManager by remember { mutableStateOf<PointAnnotationManager?>(null) }
    var pointManager by remember { mutableStateOf<PointManager?>(null) }
    var currentEvents by remember(events) { mutableStateOf(events) }
    var mapInitialized by remember { mutableStateOf(false) }
    
    val mapView = remember {
        MapView(context).apply {
            getMapboxMap().loadStyle(Style.MAPBOX_STREETS)
            camera(initialCameraPosition)
        }
    }
    
    // Manejo de errores de inicialización
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    mapView.onStart()
                }
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    // Inicializar gestores de anotaciones cuando el mapa esté listo
    LaunchedEffect(mapView) {
        try {
            // Esperar a que el estilo se cargue completamente
            val mapboxMap = mapView.getMapboxMap()
            
            // Crear gestor de anotaciones puntuales
            pointAnnotationManager = mapboxMap.annotations.createPointAnnotationManager(
                AnnotationConfig()
            )
            
            // Configurar listener de clicks para anotaciones
            pointAnnotationManager?.addClickListener { annotation ->
                val eventId = annotation.title
                if (eventId.isNotEmpty()) {
                    onEventClick(eventId)
                }
                true
            }
            
            // Crear gestor de clustering (PointManager)
            pointManager = mapboxMap.annotations.createPointManager(
                AnnotationConfig()
                    .withIconImage("marker-icon") // Icono por defecto para clusters
            )
            
            mapInitialized = true
            
            // Actualizar eventos iniciales
            updateEventsOnMap(currentEvents)
        } catch (e: Exception) {
            e.printStackTrace()
            // En un entorno de producción, mostraría un Toast o Snackbar al usuario
            // Toast.makeText(context, "Error al cargar el mapa: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    // Actualizar eventos cuando cambien
    LaunchedEffect(mapInitialized, currentEvents) {
        if (mapInitialized) {
            updateEventsOnMap(currentEvents)
        }
    }
    
    // Actualizar ubicación del usuario si se proporciona
    LaunchedEffect(mapInitialized, userLocation) {
        if (mapInitialized && userLocation != null) {
            val point = Point.fromLngLat(userLocation.longitude, userLocation.latitude)
            val cameraOptions = CameraOptions.Builder()
                .center(point)
                .zoom(CoordinateDefaults.CITY_ZOOM)
                .build()
            mapView.getMapboxMap().camera().setCameraOptions(cameraOptions)
        }
    }
    
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { mapView }
    )
}

// Función auxiliar para actualizar eventos en el mapa con clustering básico
private fun updateEventsOnMap(events: List<Event>) {
    pointAnnotationManager?.let { annotationManager ->
        pointManager?.let { manager ->
            // Limpiar anotaciones existentes
            annotationManager.deleteAll()
            manager.deleteAll()
            
            // Filtrar eventos con ubicación válida
            val validEvents = events.filter { it.hasLocation }
            
            // Si hay pocos eventos, mostrarlos individualmente
            if (validEvents.size <= 10) {
                validEvents.forEach { event ->
                    val point = Point.fromLngLat(event.longitude, event.latitude)
                    val annotation = PointAnnotationOptions()
                        .withPoint(point)
                        .withTitle(event.id)
                        .withIconImage("marker-icon")
                    
                    annotationManager.create(annotation)
                }
            } else {
                // Para muchos eventos, usar clustering simple basado en cuadrícula
                // En una implementación real, se usaría un algoritmo de clustering más sofisticado
                val eventsByGrid = validEvents.groupBy { event ->
                    val latGrid = (event.latitude * 100).toInt() / 10
                    val lngGrid = (event.longitude * 100).toInt() / 10
                    Pair(latGrid, lngGrid)
                }
                
                eventsByGrid.forEach { (gridKey, gridEvents) ->
                    // Si hay muchos eventos en una celda, mostrar un cluster
                    if (gridEvents.size > 3) {
                        val avgLat = gridEvents.averageBy { it.latitude }
                        val avgLng = gridEvents.averageBy { it.longitude }
                        val point = Point.fromLngLat(avgLng, avgLat)
                        
                        val annotation = PointAnnotationOptions()
                            .withPoint(point)
                            .withTitle("cluster_${gridKey.first}_${gridKey.second}")
                            .withIconImage("marker-icon") // En un cluster real, usaríamos un icono diferente
                            .withTextField("${gridEvents.size}") // Mostrar cantidad
                        
                        manager.create(annotation)
                    } else {
                        // Mostrar eventos individuales en celdas poco pobladas
                        gridEvents.forEach { event ->
                            val point = Point.fromLngLat(event.longitude, event.latitude)
                            val annotation = PointAnnotationOptions()
                                .withPoint(point)
                                .withTitle(event.id)
                                .withIconImage("marker-icon")
                            
                            annotationManager.create(annotation)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MapboxLocationSelector(
    modifier: Modifier = Modifier,
    selectedLocation: Location? = null,
    onLocationSelected: (Double, Double) -> Unit = { _, _ -> },
    initialCameraPosition: CameraOptions = CameraOptions.Builder()
        .center(CoordinateDefaults.ARMENIA_COLOMBIA)
        .zoom(CoordinateDefaults.DEFAULT_ZOOM)
        .build()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var pointAnnotationManager by remember { mutableStateOf<PointAnnotationManager?>(null) }
    var mapInitialized by remember { mutableStateOf(false) }
    
    val mapView = remember {
        MapView(context).apply {
            getMapboxMap().loadStyle(Style.MAPBOX_STREETS)
            camera(initialCameraPosition)
        }
    }
    
    // Manejo de errores de inicialización y lifecycle
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    mapView.onStart()
                }
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    // Inicializar gestor de anotaciones cuando el mapa esté listo
    LaunchedEffect(mapView) {
        try {
            // Esperar a que el estilo se cargue completamente
            val mapboxMap = mapView.getMapboxMap()
            
            // Crear gestor de anotaciones puntuales
            pointAnnotationManager = mapboxMap.annotations.createPointAnnotationManager(
                AnnotationConfig()
            )
            
            mapInitialized = true
            
            // Actualizar ubicación inicial si se proporciona
            selectedLocation?.let { loc ->
                updateSelectedLocationOnMap(loc)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // En un entorno de producción, mostraría un Toast o Snackbar al usuario
        }
    }
    
    // Actualizar ubicación seleccionada cuando cambie
    LaunchedEffect(mapInitialized, selectedLocation) {
        if (mapInitialized) {
            selectedLocation?.let { loc ->
                updateSelectedLocationOnMap(loc)
            }
        }
    }
    
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { mapView },
        update = { view ->
            view.getMapboxMap().addOnMapClickListener { point ->
                onLocationSelected(point.latitude(), point.longitude())
                true
            }
        }
    )
}

// Función auxiliar para actualizar la ubicación seleccionada en el mapa
private fun updateSelectedLocationOnMap(location: Location) {
    pointAnnotationManager?.let { manager ->
        manager.deleteAll()
        
        val point = Point.fromLngLat(location.longitude, location.latitude)
        val annotation = PointAnnotationOptions()
            .withPoint(point)
            .withTitle("Ubicación seleccionada")
            .withIconImage("selected-marker")
        
        manager.create(annotation)
        
        // Opcional: centrar el mapa en la ubicación seleccionada
        val cameraOptions = CameraOptions.Builder()
            .center(point)
            .zoom(CoordinateDefaults.CITY_ZOOM)
            .build()
        // Nota: No actualizamos la cámara aquí para no interferir con la interacción del usuario
        // Si se quiere centrar automáticamente, descomentar la siguiente línea:
        // mapView.getMapboxMap().camera().setCameraOptions(cameraOptions)
    }
}

object CoordinateDefaults {
    val ARMENIA_COLOMBIA = Point.fromLngLat(-75.6811, 4.5339)
    const val DEFAULT_ZOOM = 13.0
    const val CITY_ZOOM = 14.0
    const val WIDE_ZOOM = 10.0
}