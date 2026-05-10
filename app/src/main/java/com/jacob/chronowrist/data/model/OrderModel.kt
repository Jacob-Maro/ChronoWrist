package com.jacob.chronowrist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderModel(
    val id: String? = null,
    @SerialName("user_id")
    val userId: String,
    @SerialName("total_amount")
    val totalAmount: Double,
    val status: String = "Processing",
    @SerialName("created_at")
    val createdAt: String? = null,
    val items: List<OrderItem> = emptyList()
)

@Serializable
data class OrderItem(
    @SerialName("watch_id")
    val watchId: Int,
    val quantity: Int,
    val price: Double
)