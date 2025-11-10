package com.example.kuit6_android_api.ui.post.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.kuit6_android_api.ui.post.component.PostItem
import com.example.kuit6_android_api.ui.post.state.PostListUiState
import com.example.kuit6_android_api.ui.post.viewmodel.PostListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    onPostClick: (Long) -> Unit,
    onCreatePostClick: () -> Unit,
    viewModel: PostListViewModel
) {
    val uiState by viewModel.postListUiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("게시글 목록") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreatePostClick) {
                Icon(Icons.Default.Add, contentDescription = "게시글 작성")
            }
        }
    ) { paddingValues ->
        when (uiState) {
            is PostListUiState.Loading -> {
                CircularProgressIndicator()
            }

            is PostListUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.background),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items((uiState as PostListUiState.Success).posts) { post ->
                        PostItem(
                            post = post,
                            onClick = { onPostClick(post.id) }
                        )
                    }
                }
            }
            is PostListUiState.Error -> {
                Text("로딩 실패")
            }
        }
    }
}
