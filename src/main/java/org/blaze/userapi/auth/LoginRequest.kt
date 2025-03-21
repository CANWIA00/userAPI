package org.blaze.userapi.auth

data class LoginRequest(
    val mail: String,
    val password: String,
)
