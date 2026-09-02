package com.michaeltchuang.ride.ui.screens.videoplayer

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.michaeltchuang.ride.ui.viewmodels.VideoPlayerScreenViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

object VideoPlayerArgs {
    const val TITLE = "title"
    const val YOUTUBE_ID = "youtubeId"
}

const val VIDEO_PLAYER_ROUTE = "videoPlayer/{${VideoPlayerArgs.TITLE}}/{${VideoPlayerArgs.YOUTUBE_ID}}"

val videoPlayerArguments =
    listOf(
        navArgument(VideoPlayerArgs.TITLE) { type = NavType.StringType },
        navArgument(VideoPlayerArgs.YOUTUBE_ID) { type = NavType.StringType },
    )

fun videoPlayerRoute(
    title: String,
    youtubeId: String,
): String = "videoPlayer/${Uri.encode(title)}/${Uri.encode(youtubeId)}"

@Composable
fun VideoPlayerRoute(
    onBackClick: () -> Unit,
    viewModel: VideoPlayerScreenViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    VideoPlayerScreen(
        title = state.title,
        youtubeId = state.youtubeId,
        onBackClick = onBackClick,
    )
}

@Composable
private fun VideoPlayerScreen(
    title: String,
    youtubeId: String,
    onBackClick: () -> Unit,
) {
    val activity = LocalActivity.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val previousOrientation = activity?.requestedOrientation
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
        onDispose {
            activity?.requestedOrientation = previousOrientation ?: ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Black),
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                YouTubePlayerView(context).apply {
                    enableAutomaticInitialization = false
                    lifecycleOwner.lifecycle.addObserver(this)
                    initialize(
                        object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                youTubePlayer.loadVideo(youtubeId, 0f)
                            }
                        },
                        IFramePlayerOptions.Builder(context).build(),
                    )
                }
            },
            onRelease = { youTubePlayerView ->
                lifecycleOwner.lifecycle.removeObserver(youTubePlayerView)
                youTubePlayerView.release()
            },
        )
        IconButton(
            onClick = onBackClick,
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
        ) {
            Text(
                text = "\u2190",
                color = Color.White,
                fontSize = 24.sp,
            )
        }
        Text(
            text = title,
            color = Color.White,
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
        )
    }
}
