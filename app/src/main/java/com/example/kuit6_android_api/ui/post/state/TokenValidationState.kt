package com.example.kuit6_android_api.ui.post.state

sealed class TokenValidationState {
    object Initial : TokenValidationState() // 검증 전 상태
    object Success : TokenValidationState()
    object Failure : TokenValidationState()
}