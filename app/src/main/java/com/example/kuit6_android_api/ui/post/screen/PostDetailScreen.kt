package com.example.kuit6_android_api.ui.post.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kuit6_android_api.ui.post.viewmodel.PostViewModel
import com.example.kuit6_android_api.util.formatDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    postId: Long,
    onNavigateBack: () -> Unit,
    onEditClick: (Long) -> Unit = {},
    viewModel: PostViewModel = viewModel()
) {
    val post = viewModel.postDetail
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(postId) {
        viewModel.getPostDetail(postId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("게시글 상세") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(postId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "수정")
                    }
                    IconButton(onClick = {showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "삭제")
                    }
                }
            )
        }
    ) { paddingValues ->
        post?.let {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // 작성자 정보 (상단)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // 프로필 이미지
                        if (it.author.profileImageUrl != null) {
                            AsyncImage(
                                model = it.author.profileImageUrl,
                                contentDescription = "프로필 이미지",
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Surface(
                                modifier = Modifier.size(44.dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "기본 프로필",
                                    modifier = Modifier.padding(10.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = it.author.username,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = formatDateTime(it.createdAt),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // 이미지
                it.imageUrl?.let { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "게시글 이미지",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // 제목과 내용
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = it.content,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight.times(1.5f)
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }

    // 삭제 확인 다이얼로그
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("게시글 삭제") },
            text = { Text("정말로 이 게시글을 삭제하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deletePost(postId) {
                        showDeleteDialog = false
                        onNavigateBack()
                    }
                }) {
                    Text("삭제")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PostDetailScreenPreview() {
    MaterialTheme {
        PostDetailScreen(
            postId = 1L,
            onNavigateBack = {},
            onEditClick = {}
        )
    }
}
