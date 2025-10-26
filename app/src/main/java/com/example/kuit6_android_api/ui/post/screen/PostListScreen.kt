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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kuit6_android_api.ui.post.component.PostItem
import com.example.kuit6_android_api.ui.post.viewmodel.PostViewModel
import com.example.kuit6_android_api.data.model.response.AuthorResponse
import com.example.kuit6_android_api.data.model.response.PostResponse
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    onPostClick: (Long) -> Unit,
    onCreatePostClick: () -> Unit,
    viewModel: PostViewModel = viewModel()
) {
    val posts = viewModel.posts

    // 화면이 리컴포즈될 때마다 getPost 실행
    LaunchedEffect(Unit) {
        viewModel.getPosts()
    }

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
}
