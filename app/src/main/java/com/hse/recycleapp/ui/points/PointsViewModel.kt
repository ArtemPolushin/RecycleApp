package com.hse.recycleapp.ui.points

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hse.recycleapp.data.model.RecyclePoint
import com.hse.recycleapp.domain.repository.IAuthRepository
import com.hse.recycleapp.domain.repository.IRecyclePointRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PointsViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val pointRepository: IRecyclePointRepository
) : ViewModel() {
    private val _points = MutableStateFlow<List<RecyclePoint>>(listOf())
    val points: StateFlow<List<RecyclePoint>> = _points


    var pointId by mutableStateOf(null)
    var description by mutableStateOf("")
    var latitudeInput by mutableStateOf("")
    var longitudeInput by mutableStateOf("")
    var selectedCategories by mutableStateOf(setOf<Int>())

    init {
        loadPoints()
    }

    fun loadPoints() {
        viewModelScope.launch {
            val points = pointRepository.getAllPoints()
            _points.value = points
        }
    }

    fun getPointById(id: String): RecyclePoint? = _points.value.find { it.id == id }

    suspend fun savePoint(point: RecyclePoint) {
        _points.value = _points.value
            .filterNot { it.id == point.id } + point
        pointRepository.addPoint(point)
    }

    suspend fun deletePoint(id: String) {
        _points.value = _points.value.filterNot { it.id == id }
        pointRepository.removePoint(id)
    }
}