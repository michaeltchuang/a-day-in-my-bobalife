package com.michaeltchuang.ride.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaeltchuang.ride.data.DataProvider
import com.michaeltchuang.ride.data.models.Video
import com.michaeltchuang.ride.ui.EventDelegate
import com.michaeltchuang.ride.ui.EventViewModel
import com.michaeltchuang.ride.ui.StateDelegate
import com.michaeltchuang.ride.ui.StateViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

data class MainState(
    val videos: List<Video> = emptyList(),
)

sealed interface MainViewEvent {
    data class PlayVideo(
        val title: String,
        val youtubeId: String,
    ) : MainViewEvent
}

class MainScreenViewModel :
    ViewModel(),
    StateViewModel<MainState>,
    EventViewModel<MainViewEvent> {
    private val stateDelegate =
        StateDelegate<MainState>().apply {
            setDefaultState(MainState(videos = DataProvider.videos))
        }
    private val eventDelegate = EventDelegate<MainViewEvent>()

    override val state: StateFlow<MainState>
        get() = stateDelegate.state

    override val viewEvent: Flow<MainViewEvent>
        get() = eventDelegate.viewEvent

    fun onVideoClicked(video: Video) {
        eventDelegate.sendEvent(
            viewModelScope,
            MainViewEvent.PlayVideo(title = video.title, youtubeId = video.youtubeId),
        )
    }
}
