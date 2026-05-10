package com.jacob.chronowrist.data.repository

import com.jacob.chronowrist.data.model.UserModel
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepository : AuthService {
    private val supabase = createSupabaseClient(
        supabaseUrl = "https://iklozyanrrjtnozykvnm.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImlrbG96eWFucnJqdG5venlrdm5tIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgxNzg3NDksImV4cCI6MjA5Mzc1NDc0OX0.edxM4qRuxU7wjmeFGfG85YWMu5FaJMQyyiJGxGiSpVc"
    ) {
        install(Postgrest)
        install(Auth)
    }

    override suspend fun registerUser(user: UserModel) {
        // 1. Sign up the user with Supabase Auth
        val authResult = supabase.auth.signUpWith(Email) {
            email = user.email
            password = user.password
            data = buildJsonObject {
                put("full_name", user.fullName)
            }
        }

        // 2. Save/Update profile in 'profiles' table using upsert to avoid duplicate key errors
        val userId = authResult?.id
        if (userId != null) {
            val profileData = mapOf(
                "id" to userId,
                "full_name" to user.fullName,
                "email" to user.email,
                "balance" to 1000.0 // Default starting balance
            )
            // upsert prevents duplicate key errors by updating if exists
            supabase.postgrest["profiles"].upsert(profileData)
        }
    }

    override suspend fun loginUser(user: UserModel) {
        supabase.auth.signInWith(Email) {
            email = user.email
            password = user.password
        }
    }

    override suspend fun resetPassword(email: String) {
        supabase.auth.resetPasswordForEmail(email = email)
    }

    override suspend fun getUserProfile(): UserModel? {
        val userId = getCurrentUserId() ?: return null
        return try {
            supabase.postgrest["profiles"].select {
                filter {
                    eq("id", userId)
                }
            }.decodeSingle<UserModel>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun logoutUser() {
        supabase.auth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }
}