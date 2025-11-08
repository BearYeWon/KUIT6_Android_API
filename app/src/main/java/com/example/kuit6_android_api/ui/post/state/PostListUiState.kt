package com.example.kuit6_android_api.ui.post.state

import com.example.kuit6_android_api.data.model.response.PostResponse

sealed class PostListUiState{
    data object Loading: PostListUiState()
    
    data class Success(
        val posts: List<PostResponse>
    ) : PostListUiState()
    
    data class Error (
        val message: String
    ) : PostListUiState()
}