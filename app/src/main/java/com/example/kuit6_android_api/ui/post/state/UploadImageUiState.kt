package com.example.kuit6_android_api.ui.post.state

import com.example.kuit6_android_api.data.model.response.PostResponse

sealed class UploadImageUiState {
    data object Idle : UploadImageUiState()
    data object Loading : UploadImageUiState()

    data class Success(
        val imgUrl: Map<String, String>
    ) : UploadImageUiState()

    data class Error(
        val message: String
    ) : UploadImageUiState()
}