package com.example.kuit6_android_api.ui.post.state

sealed class UploadImageUiState {
    data object Idle : UploadImageUiState() // 업로드가 아닌 일반 상태
    data object Loading : UploadImageUiState() // 업로딩 상태

    data class Success(
        val imgUrl: Map<String, String>
    ) : UploadImageUiState()

    data class Error(
        val message: String
    ) : UploadImageUiState()
}