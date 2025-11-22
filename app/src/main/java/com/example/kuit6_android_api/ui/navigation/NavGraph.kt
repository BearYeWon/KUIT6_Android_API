package com.example.kuit6_android_api.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.kuit6_android_api.ui.post.screen.LoginScreen
import com.example.kuit6_android_api.ui.post.screen.PostCreateScreen
import com.example.kuit6_android_api.ui.post.screen.PostDetailScreen
import com.example.kuit6_android_api.ui.post.screen.PostEditScreen
import com.example.kuit6_android_api.ui.post.screen.PostListScreen

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
                onLoginClick = {
                    navController.navigate(LoginRoute)
                },
                // 다른 뷰모델에는 이름만 바꿔서 적용
                viewModel = hiltViewModel()
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
                viewModel = hiltViewModel()
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
                viewModel = hiltViewModel()
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
                viewModel = hiltViewModel()
            )
        }

        composable<LoginRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<LoginRoute>()

            LoginScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = hiltViewModel()
            )
        }
    }
}

