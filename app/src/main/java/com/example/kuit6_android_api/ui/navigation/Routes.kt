package com.example.kuit6_android_api.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object PostListRoute

@Serializable
data class PostDetailRoute(val postId: Long)

@Serializable
object PostCreateRoute

@Serializable
data class PostEditRoute(val postId: Long)

@Serializable
object LoginRoute