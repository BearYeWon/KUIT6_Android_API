package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _PostList_uiState =
        MutableStateFlow<PostListUiState>(PostListUiState.Loading) // 변경 가능 상태
    val postListUiState: StateFlow<PostListUiState> = _PostList_uiState.asStateFlow() // 읽기 전용

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _PostList_uiState.value = PostListUiState.Loading

            postRepository.getPosts()
                .onSuccess { posts ->
                    _PostList_uiState.value = PostListUiState.Success(posts)
                }
                .onFailure { error ->
                    _PostList_uiState.value =
                        PostListUiState.Error(error.message ?: "error")
                }
        }
    }

    fun refresh() {
        loadPosts()
    }
}

