package com.example.proyectofinaldisenomovil.core.component.mapbox

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.R
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.DefaultViewportTransitionOptions

@Composable
fun MapBox(
    modifier: Modifier = Modifier,
    showMyLocationButton: Boolean = true,
    event : Event?,
    activateClick: Boolean,
    onMapClickListener: (Point) -> Unit = {} // Callback para manejar el clic en el mapa
) {

    // Estado para manejar permisos de ubicación
    val permissionState = rememberLocationPermissionState()
    var shouldFollowUser by remember { mutableStateOf(false) }

    // Configurar el estado inicial del mapa
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(10.0)
            center(Point.fromLngLat(-75.6491181, 4.4687891))
        }
    }

    val marker = rememberIconImage(
        key = com.example.proyectofinaldisenomovil.R.drawable.marker,
        painter = painterResource(com.example.proyectofinaldisenomovil.R.drawable.marker)
    )

    var clickedPoint by remember { mutableStateOf<Point?>(null) }

    Box(modifier = modifier) {

        MapboxMap(
            modifier = Modifier.matchParentSize(),
            mapViewportState = mapViewportState,
            onMapClickListener = { point ->
                // Manejar el clic en el mapa solo si está activado
                if (activateClick) {
                    onMapClickListener(point) // Llamar al callback externo y se le pasa el punto
                    clickedPoint = point
                }
                true
            }
        ){
            // Configurar ubicación del usuario si tiene permiso y quiere seguirla
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

            clickedPoint?.let { point ->
                PointAnnotation(point = point) {
                    iconImage = marker
                }
            }

            if(event != null){
                PointAnnotation(
                    point = Point.fromLngLat(event.longitude, event.latitude)
                ) {
                    iconImage = marker
                }
            }


        }

        // Botón de mi ubicación
        if (showMyLocationButton) {
            FloatingActionButton(
                onClick = {
                    if (permissionState.hasPermission) {
                        shouldFollowUser = true
                    } else {
                        permissionState.requestPermission() // Solicitar permiso si no lo tiene
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Mi ubicación"
                )
            }
        }
    }

}


/**
 * Estado para manejar el permiso de ubicación de forma controlada
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
    permission: String = android.Manifest.permission.ACCESS_FINE_LOCATION
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