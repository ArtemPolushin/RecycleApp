package com.hse.recycleapp.data

enum class WasteType(val id: Int, val displayName: String) {
    CARDBOARD(1, "Картон"),
    GLASS(2, "Стекло"),
    METAL(3, "Металл"),
    PAPER(4, "Бумага"),
    PLASTIC(5, "Пластик"),
    TRASH(6, "Не определено"),
}