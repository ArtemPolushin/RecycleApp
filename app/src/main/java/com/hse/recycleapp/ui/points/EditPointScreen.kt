package com.hse.recycleapp.ui.points

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hse.recycleapp.data.WasteType
import com.hse.recycleapp.data.model.RecyclePoint
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch

@Composable
fun EditPointScreen(
    navController: NavHostController,
    viewModel: PointsViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var pointId by viewModel::pointId
    var description by viewModel::description
    var latitudeInput by viewModel::latitudeInput
    var longitudeInput by viewModel::longitudeInput
    var selectedCategories by viewModel::selectedCategories

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val selectedLat = savedStateHandle?.get<Double>("selectedLat")
    val selectedLng = savedStateHandle?.get<Double>("selectedLng")

    LaunchedEffect(Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            navController.navigate("login?redirect=addPoint") {
                popUpTo("addPoint") { inclusive = true }
            }
        }
    }

    LaunchedEffect(selectedLat, selectedLng) {
        if (selectedLat != null && selectedLng != null) {
            latitudeInput = selectedLat.toString()
            longitudeInput = selectedLng.toString()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Добавить пункт приёма", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = latitudeInput,
                onValueChange = { latitudeInput = it },
                label = { Text("Широта") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = longitudeInput,
                onValueChange = { longitudeInput = it },
                label = { Text("Долгота") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { navController.navigate("selectLocation") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Выбрать координаты на карте")
            }

            Text("Категории отходов", style = MaterialTheme.typography.titleMedium)
            WasteType.entries.filter { it != WasteType.TRASH }.forEach { type ->
                val isSelected = selectedCategories.contains(type.id)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = {
                            selectedCategories = if (isSelected) {
                                selectedCategories - type.id
                            } else {
                                selectedCategories + type.id
                            }
                        }
                    )
                    Text(type.displayName)
                }
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            val lat = latitudeInput.toDoubleOrNull()
                            val lng = longitudeInput.toDoubleOrNull()
                            if (description.isBlank()) {
                                snackbarHostState.showSnackbar("Введите описание пункта")
                                return@launch
                            }
                            if (selectedCategories.isEmpty()) {
                                snackbarHostState.showSnackbar("Выберите хотя бы одну категорию отходов")
                                return@launch
                            }
                            if (lat == null || lng == null) {
                                snackbarHostState.showSnackbar("Введите корректные координаты")
                                return@launch
                            }

                            val point = RecyclePoint(
                                id = null,
                                description = description,
                                location = GeoPoint(lat, lng),
                                wasteTypeIds = selectedCategories.toList()
                            )
                            viewModel.savePoint(point)
                            snackbarHostState.showSnackbar("Пункт успешно сохранён")
                            navController.popBackStack()
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Ошибка: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }

            OutlinedButton(
                onClick = { navController.navigate("map") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Открыть карту")
            }
        }
    }
}
