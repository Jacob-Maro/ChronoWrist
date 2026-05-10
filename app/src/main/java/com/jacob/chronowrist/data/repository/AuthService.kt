package com.jacob.chronowrist.data.repository

import com.jacob.chronowrist.data.model.UserModel

interface AuthService {
    suspend fun registerUser(user: UserModel)
    suspend fun loginUser(user: UserModel)
    suspend fun resetPassword(email: String)
    suspend fun getUserProfile(): UserModel?
    suspend fun logoutUser()
    fun getCurrentUserId(): String?
}