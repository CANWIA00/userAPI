package org.blaze.userapi.auth

data class RegisterRequest(
    val email: String,
    val password: String,
)
