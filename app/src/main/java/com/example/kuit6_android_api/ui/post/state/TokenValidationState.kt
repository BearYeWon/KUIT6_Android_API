package com.example.kuit6_android_api.ui.post.state

sealed class TokenValidationState {
    object Initial : TokenValidationState()
    object Success : TokenValidationState()
    object Failure : TokenValidationState()
}