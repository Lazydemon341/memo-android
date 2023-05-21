package com.memo.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.memo.account.api.navigation.loginScreen
import com.memo.account.api.navigation.navigateToSignUp
import com.memo.account.api.navigation.signUpScreen
import com.memo.camera.api.navigation.cameraNavigationRoute
import com.memo.camera.api.navigation.cameraScreen
import com.memo.camera.api.navigation.navigateToCamera
import com.memo.chat.api.navigation.chatScreen
import com.memo.chat.api.navigation.navigateToChat
import com.memo.chats.list.api.navigation.chatsListScreen
import com.memo.feed.api.feedScreen
import com.memo.friends.map.api.friendsMapScreen
import com.memo.friendship.api.friendshipScreen
import com.memo.memories.api.navigation.memoryScreen
import com.memo.memories.api.navigation.navigateToMemory
import com.memo.memories.generation.api.memoriesAutoGeneration
import com.memo.memories.upload.api.memoryUpload
import com.memo.memories.upload.api.navigateToMemoryUpload
import com.memo.post.on.map.api.navigateToPostOnMap
import com.memo.post.on.map.api.postOnMap
import com.memo.publish_photo.api.navigation.navigateToPublishPhoto
import com.memo.publish_photo.api.navigation.publishPhotoScreen
import com.memo.user.profile.api.navigateToUserProfile
import com.memo.user.profile.api.userProfileScreen

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = cameraNavigationRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        loginScreen(
            onBackPressed = onBackClick,
            onNavigateToSignUp = navController::navigateToSignUp,
            onLoginSuccess = navController::navigateToCamera,
            nestedGraphs = {
                signUpScreen(onBackClick)
            }
        )
        memoryScreen(onBackClick)
        cameraScreen(
            onNavigateToPublishPhoto = navController::navigateToPublishPhoto,
            nestedGraphs = {
                publishPhotoScreen(
                    onBackPressed = onBackClick,
                )
            }
        )
        chatsListScreen(
            onBackPressed = onBackClick,
            onNavigateToChat = navController::navigateToChat,
            nestedGraphs = {
                chatScreen(onBackPressed = onBackClick)
            }
        )
        friendshipScreen(
            onBackPressed = onBackClick,
            onNavigateToUserProfile = navController::navigateToUserProfile,
        )
        userProfileScreen(
            onBackPressed = onBackClick,
            onNavigateToMemory = navController::navigateToMemory,
        )
        memoryUpload(onBackClick)
        feedScreen(
            onBackPressed = onBackClick,
            onNavigateToUserProfile = navController::navigateToUserProfile,
        )
        memoriesAutoGeneration(
            onBackPressed = onBackClick,
            onNavigateToMemoryUpload = navController::navigateToMemoryUpload
        )
        friendsMapScreen(
            onBackPressed = onBackClick,
            onNavigateToPost = navController::navigateToPostOnMap,
            nestedGraphs = {
                postOnMap(onBackClick)
            }
        )
    }
}
