package com.example.kuit6_android_api.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.kuit6_android_api.ui.post.screen.PostCreateScreen
import com.example.kuit6_android_api.ui.post.screen.PostDetailScreen
import com.example.kuit6_android_api.ui.post.screen.PostEditScreen
import com.example.kuit6_android_api.ui.post.screen.PostListScreen
import com.example.kuit6_android_api.ui.post.viewmodel.PostCreateViewModel
import com.example.kuit6_android_api.ui.post.viewmodel.PostDetailViewModel
import com.example.kuit6_android_api.ui.post.viewmodel.PostEditViewModel
import com.example.kuit6_android_api.ui.post.viewmodel.PostListViewModel
import com.example.kuit6_android_api.ui.post.viewmodel.postViewModelFactory

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Any = PostListRoute
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<PostListRoute> {
            PostListScreen(
                onPostClick = { postId ->
                    navController.navigate(PostDetailRoute(postId))
                },
                onCreatePostClick = {
                    navController.navigate(PostCreateRoute)
                },
                // 다른 뷰모델에는 이름만 바꿔서 적용
                viewModel = viewModel(factory = postViewModelFactory { PostListViewModel(postRepository = it) })
            )
        }

        composable<PostDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PostDetailRoute>()

            PostDetailScreen(
                postId = route.postId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onEditClick = { postId ->
                    navController.navigate(PostEditRoute(postId))
                },
                viewModel = viewModel(factory = postViewModelFactory {
                    PostDetailViewModel(
                        postRepository = it
                    )
                })
            )
        }

        composable<PostCreateRoute> {
            PostCreateScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPostCreated = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("refreshNeeded", true)
                    navController.popBackStack()
                },
                viewModel = viewModel(factory = postViewModelFactory {
                    PostCreateViewModel(
                        postRepository = it
                    )
                })
            )
        }

        composable<PostEditRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PostEditRoute>()

            PostEditScreen(
                postId = route.postId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPostUpdated = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("refreshNeeded", true)
                    navController.popBackStack()
                },
                viewModel = viewModel(factory = postViewModelFactory {
                    PostEditViewModel(
                        postRepository = it
                    )
                })
            )
        }
    }
}

