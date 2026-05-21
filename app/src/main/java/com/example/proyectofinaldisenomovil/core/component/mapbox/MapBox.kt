package com.example.proyectofinaldisenomovil.core.component.mapbox

import android.Manifest
import com.example.proyectofinaldisenomovil.R

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.DefaultViewportTransitionOptions

@Composable
fun MapBox(
    modifier: Modifier = Modifier,
    showMyLocationButton: Boolean = true,
    event : Event?,
    activateClick: Boolean,
    onMapClickListener: (Point) -> Unit = {}
) {
    val view = LocalView.current
    val permissionState = rememberLocationPermissionState()
    var shouldFollowUser by remember { mutableStateOf(false) }
    var isMaximized by remember { mutableStateOf(false) }

    val mapViewportState = rememberMapViewportState {
        if(event != null){
            setCameraOptions {
                zoom(10.0)
                center(Point.fromLngLat(event.longitude, event.latitude))
            }
        }
    }

    val marker = rememberIconImage(
        key = "marker_icon",
        painter = painterResource(R.drawable.marker)
    )

    var clickedPoint by remember { mutableStateOf<Point?>(null) }

    // Función que contiene el mapa y sus controles para ser reutilizada
    @Composable
    fun MapContent(
        innerModifier: Modifier,
        maximized: Boolean,
        onToggleMaximize: () -> Unit
    ) {
        Box(modifier = innerModifier) {
            MapboxMap(
                modifier = Modifier.matchParentSize(),
                mapViewportState = mapViewportState,
                onMapClickListener = { point ->
                    if (activateClick) {
                        onMapClickListener(point)
                        clickedPoint = point
                    }
                    true
                }
            ){
                if (permissionState.hasPermission && shouldFollowUser) {
                    MapEffect(key1 = "follow_puck") { mapView ->
                        mapView.location.updateSettings {
                            locationPuck = createDefault2DPuck(withBearing = true)
                            enabled = true
                            puckBearing = PuckBearing.COURSE
                            puckBearingEnabled = true
                        }
                        mapViewportState.transitionToFollowPuckState(
                            defaultTransitionOptions = DefaultViewportTransitionOptions.Builder()
                                .maxDurationMs(1500)
                                .build()
                        )
                    }
                }

                // Favor clickedPoint for pin display (allows moving the pin during edit)
                val pinPoint = clickedPoint ?: event?.let { Point.fromLngLat(it.longitude, it.latitude) }
                
                pinPoint?.let { point ->
                    PointAnnotation(point = point) {
                        iconImage = marker
                    }
                }
            }

            // Botón de Cerrar (X) si está maximizado
            if (maximized) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    IconButton(onClick = onToggleMaximize) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                    }
                }
            }

            // Columna de controles
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botón Maximizar (Solo si no está ya maximizado)
                if (!maximized) {
                    SmallFloatingActionButton(
                        onClick = onToggleMaximize,
                        modifier = Modifier.padding(bottom = 8.dp),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(Icons.Default.Fullscreen, contentDescription = "Maximizar")
                    }
                }

                // Botón Acercar (+)
                SmallFloatingActionButton(
                    onClick = {
                        val currentZoom = mapViewportState.cameraState?.zoom ?: 10.0
                        mapViewportState.flyTo(
                            CameraOptions.Builder()
                                .zoom(currentZoom + 1.0)
                                .build()
                        )
                    },
                    modifier = Modifier.padding(bottom = 8.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Acercar")
                }

                // Botón Alejar (-)
                SmallFloatingActionButton(
                    onClick = {
                        val currentZoom = mapViewportState.cameraState?.zoom ?: 10.0
                        mapViewportState.flyTo(
                            CameraOptions.Builder()
                                .zoom(currentZoom - 1.0)
                                .build()
                        )
                    },
                    modifier = Modifier.padding(bottom = 12.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Alejar")
                }

                // Botón de mi ubicación
                if (showMyLocationButton) {
                    FloatingActionButton(
                        onClick = {
                            if (permissionState.hasPermission) {
                                shouldFollowUser = true
                            } else {
                                permissionState.requestPermission()
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Mi ubicación"
                        )
                    }
                }
            }
        }
    }

    // Contenedor principal
    Box(modifier = modifier
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    awaitPointerEvent(PointerEventPass.Initial)
                    view.parent?.requestDisallowInterceptTouchEvent(true)
                }
            }
        }
    ) {
        // Renderizado normal en la lista
        MapContent(
            innerModifier = Modifier.matchParentSize(),
            maximized = false,
            onToggleMaximize = { isMaximized = true }
        )

        // Diálogo para el modo maximizado
        if (isMaximized) {
            Dialog(
                onDismissRequest = { isMaximized = false },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false, // Permite pantalla completa
                    dismissOnBackPress = true,
                    dismissOnClickOutside = false
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapContent(
                        innerModifier = Modifier.fillMaxSize(),
                        maximized = true,
                        onToggleMaximize = { isMaximized = false }
                    )
                }
            }
        }
    }
}

/**
 * Estado para manejar el permiso de ubicación de forma controlada
 */
class LocationPermissionState(
    hasPermission: Boolean = false,
    val requestPermission: () -> Unit = {}
) {
    var hasPermission by mutableStateOf(hasPermission)
        internal set

    var wasJustGranted by mutableStateOf(false)
        internal set
}



@Composable
fun rememberLocationPermissionState(
    permission: String = Manifest.permission.ACCESS_FINE_LOCATION
): LocationPermissionState {
    val context = LocalContext.current

    // Verificar el estado inicial del permiso
    val initialPermission = remember {
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    // Estado para manejar el permiso
    val state = remember { LocationPermissionState(hasPermission = initialPermission) }

    // Lanzador para solicitar el permiso
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        state.wasJustGranted = granted && !state.hasPermission
        state.hasPermission = granted
    }

    // Recordar el estado del permiso
    return remember(state, launcher) {
        LocationPermissionState(
            hasPermission = state.hasPermission,
            requestPermission = { launcher.launch(permission) }
        ).also {
            it.wasJustGranted = state.wasJustGranted
        }
    }
}
