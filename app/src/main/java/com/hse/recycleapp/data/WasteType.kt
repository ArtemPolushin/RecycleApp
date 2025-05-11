package com.hse.recycleapp.data

enum class WasteType(val id: Int, val displayName: String) {
    PLASTIC(1, "Пластик"),
    GLASS(2, "Стекло"),
    METAL(3, "Металл"),
    ELECTRONICS(4, "Электроника"),
    PAPER(5, "Бумага"),
    TRASH(6, "Остальное"),
}
