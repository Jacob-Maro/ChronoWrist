package com.jacob.chronowrist.data.model

data class Watch(
    val id: Int,
    val brand: String,
    val name: String,
    val price: Double,
    val oldPrice: Double? = null,
    val category: WatchCategory,
    val badge: WatchBadge? = null,
    val rating: Int,
    val reviewCount: Int,
    val description: String,
    val specs: WatchSpecs,
    val imageRes: Int? = null  // set to a drawable res if you have real images
)

data class WatchSpecs(
    val movement: String,
    val caseSize: String,
    val crystal: String,
    val waterResistance: String
)

enum class WatchCategory(val label: String) {
    DRESS("Dress"),
    SPORT("Sport"),
    DIVE("Dive"),
    CHRONO("Chrono")
}

enum class WatchBadge(val label: String) {
    NEW("New"),
    SALE("Sale"),
    HOT("Hot")
}

data class CartItem(
    val watch: Watch,
    val quantity: Int
)
