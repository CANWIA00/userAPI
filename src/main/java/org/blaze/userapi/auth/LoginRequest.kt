package org.blaze.userapi.auth

data class LoginRequest(
    val email: String,
    val password: String,
)
