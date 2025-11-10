package com.example.kuit6_android_api.ui.post.state

import com.example.kuit6_android_api.data.model.response.PostResponse

sealed class PostEditUiState {
    data object Loading : PostEditUiState()

    data class Success(
        val post: PostResponse
    ) : PostEditUiState()

    data class Error(
        val message: String
    ) : PostEditUiState()
}