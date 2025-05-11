package com.hse.recycleapp.data.model

import com.google.firebase.firestore.GeoPoint
data class RecyclePoint(
    var id: String?,
    var description: String = "",
    var location: GeoPoint = GeoPoint(0.0, 0.0),
    var wasteTypeIds: List<Int> = emptyList<Int>()
) {
    constructor() : this(null, "", GeoPoint(0.0, 0.0), emptyList())
}