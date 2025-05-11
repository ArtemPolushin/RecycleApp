package com.hse.recycleapp.data.repository

import com.hse.recycleapp.data.model.RecyclePoint
import com.hse.recycleapp.domain.repository.IRecyclePointRepository
import javax.inject.Inject

class RecyclePointRepositoryLocal  @Inject constructor() : IRecyclePointRepository {

    private var pointsList = mutableListOf<RecyclePoint>()

    override suspend fun addPoint(recyclePoint: RecyclePoint) {
        pointsList.add(recyclePoint)
    }

    override suspend fun getAllPoints(): List<RecyclePoint> {
        return pointsList
    }

    override suspend fun removePoint(pointId: String) {
        pointsList.removeAll { it.id == pointId }
    }

    override suspend fun getPointById(pointId: String): RecyclePoint? {
        return pointsList.find { it.id == pointId }
    }
}