package com.michaeltchuang.ride.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.michaeltchuang.ride.ui.StateDelegate
import com.michaeltchuang.ride.ui.StateViewModel
import com.michaeltchuang.ride.ui.screens.videoplayer.VideoPlayerArgs
import kotlinx.coroutines.flow.StateFlow

data class VideoPlayerState(
    val title: String = "",
    val youtubeId: String = "",
)

class VideoPlayerScreenViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel(),
    StateViewModel<VideoPlayerState> {
    private val stateDelegate =
        StateDelegate<VideoPlayerState>().apply {
            setDefaultState(
                VideoPlayerState(
                    title = savedStateHandle.get<String>(VideoPlayerArgs.TITLE).orEmpty(),
                    youtubeId = savedStateHandle.get<String>(VideoPlayerArgs.YOUTUBE_ID).orEmpty(),
                ),
            )
        }

    override val state: StateFlow<VideoPlayerState>
        get() = stateDelegate.state
}
