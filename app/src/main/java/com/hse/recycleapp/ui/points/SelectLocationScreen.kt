package com.hse.recycleapp.ui.points

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun SelectLocationScreen(navController: NavHostController) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(55.753564, 37.648843), 14f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("selectedLat", latLng.latitude)
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("selectedLng", latLng.longitude)
            navController.popBackStack()
        }
    )
}
