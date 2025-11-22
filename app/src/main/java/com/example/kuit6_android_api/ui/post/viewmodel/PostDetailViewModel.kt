package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.DeletePostUiState
import com.example.kuit6_android_api.ui.post.state.PostDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val postRepository : PostRepository
) : ViewModel(){
    // PostDetailUiState
    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    // DeletePostUiState
    private val _deleteUiState = MutableStateFlow<DeletePostUiState>(DeletePostUiState.Loading)
    val deleteUiState: StateFlow<DeletePostUiState> = _deleteUiState.asStateFlow()

    // 게시글 불러올 때 호출하는 함수 -> 성공 시 반환되는 response를 Success(post)로 uiState에 반환
    fun loadDetail(postId: Long){
        viewModelScope.launch {
            _uiState.value = PostDetailUiState.Loading

            // 레포지토리의 getPostDetail() 호출
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

    // 게시글 삭제할 때 호출하는 함수 -> 성공 시 response를 Success(deletePost)로 uiState에 반환
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