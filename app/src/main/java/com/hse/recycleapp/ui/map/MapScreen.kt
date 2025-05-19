package com.hse.recycleapp.ui.map

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.hse.recycleapp.data.WasteType
import com.hse.recycleapp.data.model.RecyclePoint
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavHostController,
    viewModel: MapViewModel = hiltViewModel()
) {
    val filteredPoints by viewModel.filteredPoints.collectAsState()
    var selectedCategories by remember { mutableStateOf(listOf<Int>()) }
    var isFilterVisible by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(55.753564, 37.648843), 14f)
    }
    var selectedPoint by remember { mutableStateOf<RecyclePoint?>(null) }
    val context = LocalContext.current

    fun openGoogleMapsRoute(point: RecyclePoint) {
        val lat = point.location.latitude
        val lng = point.location.longitude
        val uri = "https://www.google.com/maps/dir/?api=1&destination=$lat,$lng".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadPoints()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Фильтр") },
                navigationIcon = {
                    IconButton(onClick = { isFilterVisible = !isFilterVisible }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Фильтр"
                        )
                    }
                }
            )
        },

        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CameraAlt, contentDescription = "Определить категорию") },
                    label = { Text("Определить\nкатегорию") },
                    selected = false,
                    onClick = { navController.navigate("wasteClassifier") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Directions, contentDescription = "Построить маршрут") },
                    label = { Text("Построить\nмаршрут") },
                    selected = false,
                    enabled = selectedPoint != null,
                    onClick = { selectedPoint?.let { openGoogleMapsRoute(it) } }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AddLocationAlt, contentDescription = "Добавить пункт") },
                    label = { Text("Добавить\nпункт") },
                    selected = false,
                    onClick = {
                        navController.navigate("addPoint")
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedVisibility(visible = isFilterVisible) {
                CategoryFilterSection(
                    selected = selectedCategories,
                    onToggle = { id, isSelected ->
                        selectedCategories = if (isSelected) {
                            selectedCategories + id
                        } else {
                            selectedCategories - id
                        }
                        viewModel.setCategoryFilter(selectedCategories)
                    }
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    for (point in filteredPoints) {
                        val markerState = remember(point.id) {
                            MarkerState(position = LatLng(point.location.latitude, point.location.longitude))
                        }

                        Marker(
                            state = markerState,
                            title = point.description,
                            onClick = {
                                selectedPoint = point
                                false
                            },
                            snippet = point.wasteTypeIds
                                .mapNotNull { id -> WasteType.entries.find { it.id == id }?.displayName }
                                .joinToString(", ")
                        )
                    }
                }
            }
        }
    }
}
