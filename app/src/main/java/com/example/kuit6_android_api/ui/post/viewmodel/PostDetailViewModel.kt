package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.DeletePostUiState
import com.example.kuit6_android_api.ui.post.state.PostDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostDetailViewModel (
    private val postRepository : PostRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    private val _deleteUiState = MutableStateFlow<DeletePostUiState>(DeletePostUiState.Loading)
    val deleteUiState: StateFlow<DeletePostUiState> = _deleteUiState.asStateFlow()

    fun loadDetail(postId: Long){
        viewModelScope.launch {
            _uiState.value = PostDetailUiState.Loading

            postRepository.getPostDetail(postId)
                .onSuccess { post ->
                    _uiState.value = PostDetailUiState.Success(post)
                }
                .onFailure { exception ->
                    _uiState.value = PostDetailUiState.Error(
                        exception.message?: "error"
                    )
                }
        }
    }

    fun deletePost(postId: Long){
        viewModelScope.launch {
            _deleteUiState.value = DeletePostUiState.Loading

            postRepository.deletePost(postId)
                .onSuccess { deletePost ->
                    _deleteUiState.value = DeletePostUiState.Success(deletePost)
                }
                .onFailure { error ->
                    _deleteUiState.value = DeletePostUiState.Error(
                        error.message?: "error"
                    )
                }
        }
    }
}