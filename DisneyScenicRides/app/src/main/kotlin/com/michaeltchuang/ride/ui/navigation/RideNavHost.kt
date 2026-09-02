package com.michaeltchuang.ride.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.michaeltchuang.ride.ui.screens.main.MAIN_ROUTE
import com.michaeltchuang.ride.ui.screens.main.MainRoute
import com.michaeltchuang.ride.ui.screens.videoplayer.VIDEO_PLAYER_ROUTE
import com.michaeltchuang.ride.ui.screens.videoplayer.VideoPlayerRoute
import com.michaeltchuang.ride.ui.screens.videoplayer.videoPlayerArguments
import com.michaeltchuang.ride.ui.screens.videoplayer.videoPlayerRoute

@Composable
fun RideNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MAIN_ROUTE) {
        composable(MAIN_ROUTE) {
            MainRoute(
                onPlayVideo = { title, youtubeId ->
                    navController.navigate(videoPlayerRoute(title, youtubeId))
                },
            )
        }
        composable(
            route = VIDEO_PLAYER_ROUTE,
            arguments = videoPlayerArguments,
        ) {
            VideoPlayerRoute(onBackClick = navController::popBackStack)
        }
    }
}
