package org.blaze.userapi.auth

data class RegisterRequest(
    val mail: String,
    val password: String,
)
