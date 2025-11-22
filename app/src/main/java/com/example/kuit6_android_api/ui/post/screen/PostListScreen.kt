package com.example.kuit6_android_api.ui.post.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kuit6_android_api.ui.post.component.PostItem
import com.example.kuit6_android_api.ui.post.state.PostListUiState
import com.example.kuit6_android_api.ui.post.viewmodel.PostListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    onPostClick: (Long) -> Unit,
    onCreatePostClick: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: PostListViewModel = hiltViewModel()
) {
    val uiState by viewModel.postListUiState.collectAsState()

    // side effect로 새로 고침
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("게시글 목록") }
            )
        },
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FloatingActionButton(onClick = onLoginClick) {
                    Icon(Icons.Default.Person, contentDescription = "로그인")
                }
                FloatingActionButton(onClick = onCreatePostClick) {
                    Icon(Icons.Default.Add, contentDescription = "게시글 작성")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        when (uiState) {
            is PostListUiState.Loading -> {
                CircularProgressIndicator()
            }

            is PostListUiState.Success -> {

                //이전 Composition에서 캡처한 uiState를 다시 참조 -> Loading을 Success로 바꾸는 ClassCastException이 발생
                // -> posts를 안정된 값으로 추출, LazyColumn 내부에는 posts만 전달
                val posts = (uiState as PostListUiState.Success).posts
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.background),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(posts) { post ->
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
