package com.example.kuit6_android_api.ui.post.state

sealed class DeletePostUiState {
    data object Loading : DeletePostUiState()

    data class Success(
        val deletePost : Unit
    ) : DeletePostUiState()

    data class Error(
        val message: String
    ) : DeletePostUiState()
}