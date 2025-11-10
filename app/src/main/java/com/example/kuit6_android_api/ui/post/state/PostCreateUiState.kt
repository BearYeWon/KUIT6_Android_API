package com.example.kuit6_android_api.ui.post.state

import com.example.kuit6_android_api.data.model.response.PostResponse

sealed class PostCreateUiState {
    data object Loading : PostCreateUiState()

    data class Success(
        val post: PostResponse
    ) : PostCreateUiState()

    data class Error(
        val message: String
    ) : PostCreateUiState()
}