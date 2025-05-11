package com.hse.recycleapp.domain.repository

import com.hse.recycleapp.data.model.RecyclePoint

interface IRecyclePointRepository {
    suspend fun addPoint(recyclePoint: RecyclePoint)
    suspend fun getAllPoints(): List<RecyclePoint>
    suspend fun removePoint(pointId: String)
    suspend fun getPointById(pointId: String): RecyclePoint?
}