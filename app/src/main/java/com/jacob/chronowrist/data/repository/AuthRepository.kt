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
        // The Supabase Kotlin SDK expects the Project URL (base URL)
        // It automatically handles the /rest/v1 and /auth/v1 paths internally.
        supabaseUrl = "https://iklozyanrrjtnozykvnm.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImlrbG96eWFucnJqdG5venlrdm5tIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgxNzg3NDksImV4cCI6MjA5Mzc1NDc0OX0.edxM4qRuxU7wjmeFGfG85YWMu5FaJMQyyiJGxGiSpVc"
    ) {
        install(Postgrest)
        install(Auth)
    }

    override suspend fun registerUser(user: UserModel) {
        // 1. Sign up the user in Supabase Auth
        // We pass the full_name in the metadata to ensure it's available for database operations
        val authUser = supabase.auth.signUpWith(Email) {
            email = user.email
            password = user.password
            data = buildJsonObject {
                put("full_name", user.fullName)
            }
        }

        // 2. Manually insert the user profile into the 'profiles' table
        val userId = authUser?.id
        if (userId != null) {
            // We create a map to ensure we only send existing columns and use snake_case
            val profile = mapOf(
                "id" to userId,
                "full_name" to user.fullName,
                "email" to user.email
            )
            supabase.postgrest["profiles"].insert(profile)
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

    override suspend fun getUserProfile(user: UserModel) {
        // Implementation for fetching profile details
    }

    override suspend fun logoutUser() {
        supabase.auth.signOut()
    }
}
