package com.hse.recycleapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hse.recycleapp.data.model.RecyclePoint
import com.hse.recycleapp.domain.repository.IRecyclePointRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecyclePointRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : IRecyclePointRepository {
    private val collection = firestore.collection("recycle_points")
    override suspend fun addPoint(recyclePoint: RecyclePoint) {
        val docRef = collection.document()
        val pointWithId = recyclePoint.copy(id = docRef.id)
        docRef.set(pointWithId).await()
    }

    override suspend fun getAllPoints(): List<RecyclePoint> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(RecyclePoint::class.java) }
    }

    override suspend fun removePoint(pointId: String) {
        collection.document(pointId).delete().await()
    }

    override suspend fun getPointById(pointId: String): RecyclePoint? {
        val doc = collection.document(pointId).get().await()
        return if (doc.exists()) doc.toObject(RecyclePoint::class.java) else null
    }

}