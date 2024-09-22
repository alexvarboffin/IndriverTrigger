package com.dev.indrivertrigger.ui.authentication.model

data class SignInResponse(
    val success: Boolean,
    val token: String,
    val user: User
)
data class User(
    val created_at: String,
    val email: String,
    val email_verified_at: Any,
    val id: Int,
    val name: String,
    val password_read_only: String,
    val phone: String,
    val role_id: Int,
    val status: String,
    val updated_at: String
)