package com.michaeltchuang.ride.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.michaeltchuang.ride.R
import com.google.android.material.R as materialR

class VideoPlayerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    companion object {
        private val TAG = VideoPlayerActivity::class.java.simpleName
        const val VIDEO_TITLE_KEY = "videoTitle"
        const val VIDEO_YOUTUBE_ID_KEY = "youtubeVideoId"
        private const val YOUTUBE_KEY_IGNORE = "michaeltchuang"
        private val REQUEST_CODE = 1
    }

    private var mYouTubeId: String? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
        setTheme(materialR.style.ThemeOverlay_AppCompat_Dark)
        super.onCreate(savedInstanceState)

        val layout = layoutInflater.inflate(com.michaeltchuang.ride.R.layout.activity_video_player, null) as ConstraintLayout
        setContentView(layout)
        val videoTitle = intent.getStringExtra(VIDEO_TITLE_KEY)
        mYouTubeId = intent.getStringExtra(VIDEO_YOUTUBE_ID_KEY)

        val playerView = YouTubePlayerView(this)
        playerView.layoutParams =
            ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
            )
        layout.addView(playerView)

        playerView.initialize(YOUTUBE_KEY_IGNORE, this)

        val actionBar = actionBar ?: return
        actionBar.setDisplayHomeAsUpEnabled(true)
        if (videoTitle != null) {
            actionBar.title = videoTitle
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider,
        youTubePlayer: YouTubePlayer,
        wasRestored: Boolean,
    ) {
        if (!wasRestored) {
            youTubePlayer.loadVideo(mYouTubeId)
        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        youTubeInitializationResult: YouTubeInitializationResult?,
    ) {
        if (youTubeInitializationResult?.isUserRecoverableError == true) {
            youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show()
        } else {
            val errorMessage =
                String.format(
                    getString(R.string.error_player),
                    youTubeInitializationResult.toString(),
                )
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}
