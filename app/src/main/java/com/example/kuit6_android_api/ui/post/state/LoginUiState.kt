package com.example.kuit6_android_api.ui.post.state

data class LoginUiState(
    val id: String = "",
    val password: String = "",
    val isAutoLogin: Boolean = false,
    val token: String = "",
    // 토큰 검증 상태 추가
    val tokenValidationState: TokenValidationState = TokenValidationState.Initial, // 검증 상태는 따로 빼줌
    val isLoading: Boolean = false
)
