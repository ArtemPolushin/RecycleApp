package com.hse.recycleapp.ui.map

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hse.recycleapp.data.model.RecyclePoint
import com.hse.recycleapp.domain.repository.IRecyclePointRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: IRecyclePointRepository
) : ViewModel() {

    private val _allPoints = MutableStateFlow<List<RecyclePoint>>(emptyList())
    val allPoints: StateFlow<List<RecyclePoint>> = _allPoints

    private val _filteredPoints = MutableStateFlow<List<RecyclePoint>>(emptyList())
    val filteredPoints: StateFlow<List<RecyclePoint>> = _filteredPoints

    private var selectedCategories = listOf<Int>()

    init {
        loadPoints()
    }

    fun loadPoints() {
        viewModelScope.launch {
            val points = repository.getAllPoints()
            _allPoints.value = points
            applyFilter()
        }
    }

    fun setCategoryFilter(categories: List<Int>) {
        selectedCategories = categories
        applyFilter()
    }

    private fun applyFilter() {
        _filteredPoints.value = if (selectedCategories.isEmpty()) {
            _allPoints.value
        } else {
            _allPoints.value.filter { point ->
                point.wasteTypeIds.any { it in selectedCategories }
            }
        }
    }
}
