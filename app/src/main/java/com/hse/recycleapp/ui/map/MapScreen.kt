package com.hse.recycleapp.ui.map

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.hse.recycleapp.data.WasteType
import com.hse.recycleapp.data.model.RecyclePoint
import androidx.core.net.toUri

@Composable
fun MapScreen(
    navController: NavHostController,
    viewModel: MapViewModel = hiltViewModel()
) {
    val filteredPoints by viewModel.filteredPoints.collectAsState()
    var selectedCategories by remember { mutableStateOf(listOf<Int>()) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(55.7558, 37.6173), 10f)
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

    Column(modifier = Modifier.fillMaxSize()) {

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

        Button(
            onClick = { navController.navigate("wasteClassifier") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text("Определить категорию отхода по фото")
        }

        if (selectedPoint != null) {
            Button(
                onClick = { selectedPoint?.let { openGoogleMapsRoute(it) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Построить маршрут до точки")
            }
        }

    }
}

@Composable
fun CategoryFilterSection(
    selected: List<Int>,
    onToggle: (Int, Boolean) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .selectableGroup()
    ) {
        Text("Фильтр по категориям отходов:", style = MaterialTheme.typography.titleMedium)

        val categories = listOf(
            1 to "Пластик",
            2 to "Стекло",
            3 to "Металл",
            4 to "Электроника",
            5 to "Бумага",
            6 to "Не перерабатывается"
        )

        for ((id, name) in categories) {
            val isSelected = id in selected
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .selectable(
                        selected = isSelected,
                        onClick = { onToggle(id, !isSelected) }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onToggle(id, !isSelected) }
                )
                Text(name, Modifier.padding(start = 8.dp))
            }
        }
    }
}
