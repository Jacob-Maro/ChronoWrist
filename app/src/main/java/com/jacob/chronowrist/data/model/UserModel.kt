package com.jacob.chronowrist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: String? = null,
    @SerialName("full_name")
    val fullName: String = "",
    val email: String = "",
    val password: String = ""
)